package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.ClienteDTO;
import tesis.dtos.PersonaDTO;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ReseniaDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.ProfesionistaEntity;
import tesis.entities.ReseniaEntity;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.Resenia;
import tesis.repositories.ClienteJpaRepository;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.ProfesionistaJpaRepository;
import tesis.repositories.ReseniaJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.ReseniaService;
import tesis.services.UsuarioService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReseniaServiceImpl implements ReseniaService {
    @Autowired
    ReseniaJpaRepository reseniaJpaRepository;
    @Autowired
    ProfesionistaJpaRepository profesionistaJpaRepository;
    @Autowired
    ClienteJpaRepository clienteJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<Resenia> obtenerReseñas() {
        List<Resenia> lst;
        try{
            List<ReseniaEntity> lista= reseniaJpaRepository.findAll();
            lst = mapearListaReseñas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<Resenia> obtenerReseñasByCliente(Long idCliente) {
        List<Resenia> lst;
        try{
            List<ReseniaEntity> lista= reseniaJpaRepository.findAllByCliente_Id(idCliente);
            lst = mapearListaReseñas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<Resenia> obtenerReseñasByProfesionista(Long idProfesionista) {
        List<Resenia> lst;
        try{
            List<ReseniaEntity> lista= reseniaJpaRepository.findAllByProfesionista_Id(idProfesionista);
            lst = mapearListaReseñas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public Resenia obtenerReseñaById(Long idReseña) {
        Optional<ReseniaEntity> optionalEntity = reseniaJpaRepository.findById(idReseña);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        ReseniaEntity r = optionalEntity.get();
        Resenia resenia = modelMapper.map(r, Resenia.class);
        ClienteDTO clienteDTO = modelMapper.map(r.getCliente(),ClienteDTO.class);
        clienteDTO.setPersona(modelMapper.map(r.getCliente().getPersona(), PersonaDTO.class));
        if(r.getCliente().getPersona().getCiudad() != null)
            clienteDTO.getPersona().setCiudad(r.getCliente().getPersona().getCiudad().getDescripcion());
        if(r.getCliente().getPersona().getTipoDNI() != null)
            clienteDTO.getPersona().setTipoDNI(r.getCliente().getPersona().getTipoDNI().getDescripcion());
        clienteDTO.getPersona().setEmailUsuario(r.getCliente().getPersona().getUsuario().getEmail());

        ProfesionistaDTO profesionistaDTO = modelMapper.map(r.getProfesionista(),ProfesionistaDTO.class);
        profesionistaDTO.setPersona(modelMapper.map(r.getProfesionista().getPersona(), PersonaDTO.class));
        if(r.getProfesionista().getPersona().getCiudad() != null)
            profesionistaDTO.getPersona().setCiudad(r.getProfesionista().getPersona().getCiudad().getDescripcion());
        if(r.getProfesionista().getPersona().getTipoDNI() != null)
            profesionistaDTO.getPersona().setTipoDNI(r.getProfesionista().getPersona().getTipoDNI().getDescripcion());
        if(r.getProfesionista().getProfesiones() != null)
            mapearProfesiones(profesionistaDTO, r.getProfesionista());
        profesionistaDTO.getPersona().setEmailUsuario(r.getProfesionista().getPersona().getUsuario().getEmail());

        resenia.setCliente(clienteDTO);
        resenia.setProfesionista(profesionistaDTO);

        return resenia;
    }

    @Override
    public MensajeRespuesta registrarReseña(ReseniaDTOPost reseniaDTOPost) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if (reseniaJpaRepository.existsByCliente_IdAndProfesionista_Id(reseniaDTOPost.getClienteId(), reseniaDTOPost.getProfesionistaId())){
                mensajeRespuesta.setMensaje("Esta persona ya realizo una reseña para este profesionista.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ReseniaEntity reseniaEntity = new ReseniaEntity(null, reseniaDTOPost.getDescripcion(), reseniaDTOPost.getCalificacion(), LocalDate.now(),null,null);
            reseniaEntity.setCliente(clienteJpaRepository.findById(reseniaDTOPost.getClienteId()).get());
            reseniaEntity.setProfesionista(profesionistaJpaRepository.findById(reseniaDTOPost.getProfesionistaId()).get());
            reseniaJpaRepository.save(reseniaEntity);

            mensajeRespuesta.setMensaje("Se ha realizado correctamente la reseña al profesionista.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al guardar la reseña.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta borrarReseñaById(Long id) {
        try{
            if(reseniaJpaRepository.existsById(id)){
                reseniaJpaRepository.deleteById(id);
                return new MensajeRespuesta("Se ha eliminado correctamente la reseña.", true);
            }else
                return new MensajeRespuesta("No se encontro la reseña a eliminar.", false);
        }catch (Exception e){
            return new MensajeRespuesta("Problemas al eliminar la reseña.", false);
        }
    }

    //Private

    private List<Resenia> mapearListaReseñas(List<ReseniaEntity> reseniaEntities) {
        List<Resenia> resenias = new ArrayList<>();
        for (ReseniaEntity r : reseniaEntities){
            Resenia resenia = modelMapper.map(r, Resenia.class);
            ClienteDTO clienteDTO = modelMapper.map(r.getCliente(),ClienteDTO.class);
            clienteDTO.setPersona(modelMapper.map(r.getCliente().getPersona(), PersonaDTO.class));
            if(r.getCliente().getPersona().getCiudad() != null)
                clienteDTO.getPersona().setCiudad(r.getCliente().getPersona().getCiudad().getDescripcion());
            if(r.getCliente().getPersona().getTipoDNI() != null)
                clienteDTO.getPersona().setTipoDNI(r.getCliente().getPersona().getTipoDNI().getDescripcion());
            clienteDTO.getPersona().setEmailUsuario(r.getCliente().getPersona().getUsuario().getEmail());

            ProfesionistaDTO profesionistaDTO = modelMapper.map(r.getProfesionista(),ProfesionistaDTO.class);
            profesionistaDTO.setPersona(modelMapper.map(r.getProfesionista().getPersona(), PersonaDTO.class));
            if(r.getProfesionista().getPersona().getCiudad() != null)
                profesionistaDTO.getPersona().setCiudad(r.getProfesionista().getPersona().getCiudad().getDescripcion());
            if(r.getProfesionista().getPersona().getTipoDNI() != null)
                profesionistaDTO.getPersona().setTipoDNI(r.getProfesionista().getPersona().getTipoDNI().getDescripcion());
            if(r.getProfesionista().getProfesiones() != null)
                mapearProfesiones(profesionistaDTO, r.getProfesionista());
            profesionistaDTO.getPersona().setEmailUsuario(r.getProfesionista().getPersona().getUsuario().getEmail());

            resenia.setCliente(clienteDTO);
            resenia.setProfesionista(profesionistaDTO);
            resenias.add(resenia);
        }
        return resenias;
    }


    public void mapearProfesiones(ProfesionistaDTO profesionistaDTO, ProfesionistaEntity profesionistaEntity) {
        List<String> profesionDTOList = new ArrayList<>();
        for (ProfesionEntity profesionEnt : profesionistaEntity.getProfesiones()) {
            profesionDTOList.add(profesionEnt.getDescripcion());
        }
        profesionistaDTO.setProfesiones(profesionDTOList);
    }
}
