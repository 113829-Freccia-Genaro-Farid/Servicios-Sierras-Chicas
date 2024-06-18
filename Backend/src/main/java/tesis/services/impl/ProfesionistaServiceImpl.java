package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tesis.dtos.AnuncioDTO;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ProfesionistaDTOPost;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.AnuncioEntity;
import tesis.entities.ProfesionistaEntity;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.AnuncioJpaRepository;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.ProfesionistaJpaRepository;
import tesis.repositories.auxiliar.ProfesionJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.ProfesionistaService;
import tesis.services.UsuarioService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Normalizer;
import java.time.YearMonth;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class ProfesionistaServiceImpl implements ProfesionistaService {
    @Autowired
    ProfesionistaJpaRepository profesionistaJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    AnuncioJpaRepository anuncioJpaRepository;
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
    public List<ProfesionistaDTO> listarProfesionistasOrdenadaSuscritos() {
        List<ProfesionistaDTO> lst;
        try{
            List<ProfesionistaEntity> lista= profesionistaJpaRepository.findAll();
            lst = mapearListaProfesionistas(lista);
            lst.sort(Comparator.comparing(ProfesionistaDTO::isSuscrito).reversed());
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<ProfesionistaDTO> listaProfesionistasConFiltros(String nombreApellido, List<Long> categorias, List<Long> profesiones, List<Long> ciudades) {
        List<ProfesionistaEntity> entityList = profesionistaJpaRepository.findAll();

        List<ProfesionistaEntity> filteredList = entityList.stream()
                .filter(profesionista -> (nombreApellido == null || nombreApellido.isEmpty() || normalizar(profesionista.getPersona().getNombre()).contains(normalizar(nombreApellido)) || normalizar(profesionista.getPersona().getApellido()).contains(normalizar(nombreApellido))))
                .filter(profesionista -> categorias == null || categorias.isEmpty() || categorias.stream().allMatch(categoria -> profesionista.getProfesiones().stream().anyMatch(p -> p.getCategoria().getId().equals(categoria))))
                .filter(profesionista -> profesiones == null || profesiones.isEmpty() || profesiones.stream().allMatch(p -> profesionista.getProfesiones().stream().anyMatch(profesion -> profesion.getId().equals(p))))
                .filter(profesionista -> ciudades == null || ciudades.isEmpty() || ciudades.stream().anyMatch(ciudad -> profesionista.getPersona().getCiudad().getId().equals(ciudad))).toList();
        List<ProfesionistaDTO> lst = mapearListaProfesionistas(filteredList);
        lst.sort(Comparator.comparing(ProfesionistaDTO::isSuscrito).reversed());
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

    @Override
    @Transactional
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
            profesionistaEntity.setSuscrito(false);
            profesionistaEntity.setPersona(personaJpaRepository.findById(profesionista.getIdPersona()).get());
            usuarioService.cambiarRolUsuario(profesionistaEntity.getPersona().getUsuario().getEmail(), rolJpaRepository.getByDescripcion("PROFESIONISTA").getId());

            ProfesionistaEntity guardado = profesionistaJpaRepository.save(profesionistaEntity);
            anuncioJpaRepository.save(new AnuncioEntity(null, new BigDecimal(BigInteger.ZERO), YearMonth.now(), guardado));

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
            profesionistaEntity.setPresentacion(profesionista.getPresentacion());
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
    public MensajeRespuesta modificarPresentacionProfesionista(Long id, String nuevaPresentacion) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            Optional<ProfesionistaEntity> optionalEntity = profesionistaJpaRepository.findById(id);
            if(optionalEntity.isEmpty()){
                mensajeRespuesta.setMensaje("El profesionista a modificar no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ProfesionistaEntity profesionistaEntity =  optionalEntity.get();
            profesionistaEntity.setPresentacion(nuevaPresentacion);
            profesionistaJpaRepository.save(profesionistaEntity);
            mensajeRespuesta.setMensaje("Se ha modificado correctamente la presentacion del profesionista.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al modificar la presentacion del profesionista.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    @Transactional
    public MensajeRespuesta borrarProfesionistaById(Long id) {
        try{
            if(profesionistaJpaRepository.existsById(id)){
                if(anuncioJpaRepository.findByProfesionista_Id(id) != null){
                    anuncioJpaRepository.deleteById(anuncioJpaRepository.findByProfesionista_Id(id).getId());
                }
                profesionistaJpaRepository.deleteById(id);
                return new MensajeRespuesta("Se ha eliminado correctamente al profesionista.", true);
            }else
                return new MensajeRespuesta("No se encontro al profesionista a eliminar.", false);
        }catch (Exception e){
            return new MensajeRespuesta("Problemas al eliminar al profesionista.", false);
        }
    }

    // ***************************************************
    // ****************** ANUNCIOS ***********************
    // ***************************************************

    @Override
    public MensajeRespuesta clickEnAnuncio(Long idProfesionista) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        AnuncioEntity anuncioEntity;
        try{
            Optional<AnuncioEntity> optionalEntity = anuncioJpaRepository.findByFechaAndProfesionista_Id(YearMonth.now(),idProfesionista);
            if (optionalEntity.isPresent()) {
                anuncioEntity = optionalEntity.get();
                anuncioEntity.setCantidadClicks(anuncioEntity.getCantidadClicks().add(BigDecimal.ONE));
            } else {
                anuncioEntity = new AnuncioEntity(null, new BigDecimal(BigInteger.ONE), YearMonth.now(), null);
                if (profesionistaJpaRepository.findById(idProfesionista).isPresent()){
                    anuncioEntity.setProfesionista(profesionistaJpaRepository.findById(idProfesionista).get());
                }else{
                    mensajeRespuesta.setMensaje("No se pudo encontrar al profesionista dueño del anuncio.");
                    mensajeRespuesta.setOk(false);
                    return mensajeRespuesta;
                }
            }
            anuncioJpaRepository.save(anuncioEntity);
            mensajeRespuesta.setMensaje("Se añadio correctamente el click al anuncio.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al añadir click en el anuncio.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<AnuncioDTO> getAnuncios() {
        List<AnuncioDTO> lst;
        try{
            List<AnuncioEntity> lista= anuncioJpaRepository.findAll();
            lst = mapearListaAnuncios(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<AnuncioDTO> getAnunciosByProfesionista(Long idProfesionista) {
        List<AnuncioDTO> lst;
        try{
            List<AnuncioEntity> lista= anuncioJpaRepository.findAllByProfesionista_Id(idProfesionista);
            lst = mapearListaAnuncios(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
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

    private List<AnuncioDTO> mapearListaAnuncios(List<AnuncioEntity> anuncioEntities) {
        List<AnuncioDTO> anuncioDTOS = new ArrayList<>();
        for (AnuncioEntity a : anuncioEntities){
            AnuncioDTO anuncioDTO = modelMapper.map(a,AnuncioDTO.class);
            if(a.getProfesionista() != null)
                anuncioDTO.setProfesionista(mapearListaProfesionistas(List.of(a.getProfesionista())).get(0));
            anuncioDTOS.add(anuncioDTO);
        }
        return anuncioDTOS;
    }

    public void mapearProfesiones(ProfesionistaDTO profesionistaDTO, ProfesionistaEntity profesionistaEntity) {
        List<String> profesionDTOList = new ArrayList<>();
        for (ProfesionEntity profesionEnt : profesionistaEntity.getProfesiones()) {
            profesionDTOList.add(profesionEnt.getDescripcion());
        }
        profesionistaDTO.setProfesiones(profesionDTOList);
    }
    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
