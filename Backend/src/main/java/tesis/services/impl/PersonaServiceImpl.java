package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.PersonaDTO;
import tesis.dtos.PersonaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.PersonaEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.auxiliar.CiudadJpaRepository;
import tesis.repositories.auxiliar.TipoDNIJpaRepository;
import tesis.services.PersonaService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    CiudadJpaRepository ciudadJpaRepository;
    @Autowired
    TipoDNIJpaRepository tipoDNIJpaRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<PersonaDTO> listarPersonas() {
        List<PersonaDTO> lst;
        try{
            List<PersonaEntity> lista= personaJpaRepository.findAll();
            lst = mapearListaPersonas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<PersonaDTO> listarPersonasFiltro(String apellido) {
        List<PersonaDTO> lst;
        try{
            List<PersonaEntity> lista= personaJpaRepository.findByApellidoStartingWith(apellido);
            lst = mapearListaPersonas(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
    @Override
    public PersonaDTO obtenerPersonaById(Long id) {
        try {
            Optional<PersonaEntity> optionalEntity = personaJpaRepository.findById(id);
            if (optionalEntity.isPresent()) {
                PersonaEntity entity = optionalEntity.get();
                PersonaDTO personaDTO = modelMapper.map(entity, PersonaDTO.class);
                if(personaDTO.getCiudad() != null)
                    personaDTO.setCiudad(entity.getCiudad().getDescripcion());
                if(personaDTO.getTipoDNI() != null)
                    personaDTO.setTipoDNI(entity.getTipoDNI().getDescripcion());
                personaDTO.setEmailUsuario(entity.getUsuario().getEmail());
                return personaDTO;
            }
            else
                return null;
        }catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
    }

    @Override
    public MensajeRespuesta modificarPersona(PersonaDTOPut personaDTO, Long id) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            Optional<PersonaEntity> optionalEntity = personaJpaRepository.findById(id);
            if(optionalEntity.isEmpty()){
                mensajeRespuesta.setMensaje("La persona a modificar no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if(personaJpaRepository.existsByNroDocumentoAndTipoDNI_Id(personaDTO.getNroDocumento(), personaDTO.getIdTipoDNI())){
                mensajeRespuesta.setMensaje("Ya existe una persona registrada con ese numero de documento.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if (personaJpaRepository.existsByTelefono1(personaDTO.getTelefono1())){
                mensajeRespuesta.setMensaje("Ya existe una persona registrada con ese numero de celular como principal.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if(Period.between(personaDTO.getFechaNacimiento(), LocalDate.now()).getYears() < 18){
                mensajeRespuesta.setMensaje("La persona debe ser mayor de 18 años.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            PersonaEntity personaEntity = optionalEntity.get();

            personaEntity.setApellido(personaDTO.getApellido());
            personaEntity.setNombre(personaDTO.getNombre());
            personaEntity.setCiudad(ciudadJpaRepository.findById(personaDTO.getIdCiudad()).get());
            personaEntity.setCalle(personaDTO.getCalle());
            personaEntity.setAltura(personaDTO.getAltura());
            personaEntity.setFechaModificacion(LocalDate.now());
            personaEntity.setFechaNacimiento(personaDTO.getFechaNacimiento());
            personaEntity.setTelefono1(personaDTO.getTelefono1());
            personaEntity.setTelefono2(personaDTO.getTelefono2());
            personaEntity.setTelefonofijo(personaDTO.getTelefonofijo());
            personaEntity.setTipoDNI(tipoDNIJpaRepository.findById(personaDTO.getIdTipoDNI()).get());
            personaEntity.setNroDocumento(personaDTO.getNroDocumento());
            personaEntity.setHabilitado(true);

            // encontrar una api para validar datos segun dni

            personaJpaRepository.save(personaEntity);
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al guardar los datos de la persona.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    // PRIVADOS

    private List<PersonaDTO> mapearListaPersonas(List<PersonaEntity> personaEntities) {
        List<PersonaDTO> personaDTOS = new ArrayList<>();
        for (PersonaEntity p:personaEntities){
            PersonaDTO personaDTO = modelMapper.map(p,PersonaDTO.class);
            if(personaDTO.getCiudad() != null)
                personaDTO.setCiudad(p.getCiudad().getDescripcion());
            if(personaDTO.getTipoDNI() != null)
                personaDTO.setTipoDNI(p.getTipoDNI().getDescripcion());
            personaDTO.setEmailUsuario(p.getUsuario().getEmail());
            personaDTOS.add(personaDTO);
        }
        return personaDTOS;
    }
}
