package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ProfesionistaDTOPost;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.ProfesionistaEntity;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.ProfesionistaJpaRepository;
import tesis.repositories.auxiliar.ProfesionJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.ProfesionistaService;
import tesis.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ProfesionistaServiceImpl implements ProfesionistaService {
    @Autowired
    ProfesionistaJpaRepository profesionistaJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    ProfesionJpaRepository profesionJpaRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<ProfesionistaDTO> listarProfesionistas() {
        List<ProfesionistaDTO> lst;
        try{
            List<ProfesionistaEntity> lista= profesionistaJpaRepository.findAll();
            lst = mapearListaProfesionistas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public ProfesionistaDTO obtenerProfesionistaById(Long id) {
        try {
            Optional<ProfesionistaEntity> optionalEntity = profesionistaJpaRepository.findById(id);
            if (optionalEntity.isPresent()) {
                ProfesionistaEntity entity = optionalEntity.get();
                ProfesionistaDTO profesionistaDTO = modelMapper.map(entity, ProfesionistaDTO.class);
                if(profesionistaDTO.getPersona().getCiudad() != null)
                    profesionistaDTO.getPersona().setCiudad(entity.getPersona().getCiudad().getDescripcion());
                if(profesionistaDTO.getPersona().getTipoDNI() != null)
                    profesionistaDTO.getPersona().setTipoDNI(entity.getPersona().getTipoDNI().getDescripcion());
                if(entity.getProfesiones() != null)
                    mapearProfesiones(profesionistaDTO, entity);
                profesionistaDTO.getPersona().setEmailUsuario(entity.getPersona().getUsuario().getEmail());
                return profesionistaDTO;
            }
            else
                return null;
        }catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
    }

    @Transactional
    @Override
    public MensajeRespuesta registrarProfesionista(ProfesionistaDTOPost profesionista) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(personaJpaRepository.findById(profesionista.getIdPersona()).isEmpty()){
                mensajeRespuesta.setMensaje("La persona a registrarse como profesionista no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if(!personaJpaRepository.findById(profesionista.getIdPersona()).get().isHabilitado()){
                mensajeRespuesta.setMensaje("No puede registrarse ya que tiene datos personales incompletos.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if (profesionistaJpaRepository.existsByPersona_Id(profesionista.getIdPersona())){
                mensajeRespuesta.setMensaje("Esta persona ya se encuentra registrada como profesionista.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ProfesionistaEntity profesionistaEntity = modelMapper.map(profesionista, ProfesionistaEntity.class);
            if(profesionista.isPoseeMatricula()){
                profesionistaEntity.setNroMatricula(profesionista.getNroMatricula());
            }else{
                profesionistaEntity.setNroMatricula(null);
            }
            for (Long idProfesion : profesionista.getIdProfesiones()) {
                if(profesionJpaRepository.findById(idProfesion).isPresent())
                    profesionistaEntity.getProfesiones().add(profesionJpaRepository.findById(idProfesion).get());
            }
            profesionistaEntity.setPersona(personaJpaRepository.findById(profesionista.getIdPersona()).get());
            usuarioService.cambiarRolUsuario(profesionistaEntity.getPersona().getUsuario().getEmail(), rolJpaRepository.getByDescripcion("PROFESIONISTA").getId());

            profesionistaJpaRepository.save(profesionistaEntity);

            mensajeRespuesta.setMensaje("Se ha guardado correctamente al profesionista.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al guardar al profesionista.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta modificarProfesionista(Long id, ProfesionistaDTOPut profesionista) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            Optional<ProfesionistaEntity> optionalEntity = profesionistaJpaRepository.findById(id);
            if(optionalEntity.isEmpty()){
                mensajeRespuesta.setMensaje("El profesionista a modificar no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ProfesionistaEntity profesionistaEntity =  optionalEntity.get();
            profesionistaEntity.setPoseeMatricula(profesionista.isPoseeMatricula());
            if(profesionista.isPoseeMatricula()){
                profesionistaEntity.setNroMatricula(profesionista.getNroMatricula());
            }else{
                profesionistaEntity.setNroMatricula(null);
            }
            profesionistaEntity.setComunicacionWsp(profesionista.isComunicacionWsp());
            for (Long idProfesion : profesionista.getIdProfesiones()) {
                if(profesionJpaRepository.findById(idProfesion).isPresent())
                    profesionistaEntity.getProfesiones().add(profesionJpaRepository.findById(idProfesion).get());
            }
            profesionistaJpaRepository.save(profesionistaEntity);
            mensajeRespuesta.setMensaje("Se ha modificado correctamente al profesionista.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al modificar al profesionista.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta borrarProfesionistaById(Long id) {
        try{
            if(profesionistaJpaRepository.existsById(id)){
                profesionistaJpaRepository.deleteById(id);
                return new MensajeRespuesta("Se ha eliminado correctamente al profesionista.", true);
            }else
                return new MensajeRespuesta("No se encontro al profesionista a eliminar.", false);
        }catch (Exception e){
            return new MensajeRespuesta("Problemas al eliminar al profesionista.", false);
        }
    }

    // PRIVADOS

    private List<ProfesionistaDTO> mapearListaProfesionistas(List<ProfesionistaEntity> profesionistaEntities) {
        List<ProfesionistaDTO> profesionistaDTOS = new ArrayList<>();
        for (ProfesionistaEntity p : profesionistaEntities){
            ProfesionistaDTO profesionistaDTO = modelMapper.map(p,ProfesionistaDTO.class);
            if(p.getPersona().getCiudad() != null)
                profesionistaDTO.getPersona().setCiudad(p.getPersona().getCiudad().getDescripcion());
            if(p.getPersona().getTipoDNI() != null)
                profesionistaDTO.getPersona().setTipoDNI(p.getPersona().getTipoDNI().getDescripcion());
            if(p.getProfesiones() != null)
                mapearProfesiones(profesionistaDTO, p);
            profesionistaDTO.getPersona().setEmailUsuario(p.getPersona().getUsuario().getEmail());
            profesionistaDTOS.add(profesionistaDTO);
        }
        return profesionistaDTOS;
    }

    public void mapearProfesiones(ProfesionistaDTO profesionistaDTO, ProfesionistaEntity profesionistaEntity) {
        List<String> profesionDTOList = new ArrayList<>();
        for (ProfesionEntity profesionEnt : profesionistaEntity.getProfesiones()) {
            profesionDTOList.add(profesionEnt.getDescripcion());
        }
        profesionistaDTO.setProfesiones(profesionDTOList);
    }
}
