package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.*;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.ProfesionistaEntity;
import tesis.entities.ReseniaEntity;
import tesis.entities.TurnoEntity;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.Turno;
import tesis.repositories.*;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.TurnoService;
import tesis.services.UsuarioService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {
    @Autowired
    TurnoJpaRepository turnoJpaRepository;
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
    public List<Turno> obtenerTurnos() {
        List<Turno> lst;
        try{
            List<TurnoEntity> lista= turnoJpaRepository.findAll();
            lst = mapearListaTurnos(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<Turno> obtenerTurnosByCliente(Long idCliente) {
        List<Turno> lst;
        try{
            List<TurnoEntity> lista= turnoJpaRepository.findAllByCliente_Id(idCliente);
            lst = mapearListaTurnos(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<Turno> obtenerTurnosByProfesionista(Long idProfesionista) {
        List<Turno> lst;
        try{
            List<TurnoEntity> lista= turnoJpaRepository.findAllByProfesionista_Id(idProfesionista);
            lst = mapearListaTurnos(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public Turno obtenerTurnoById(Long idTurno) {
        Optional<TurnoEntity> optionalEntity = turnoJpaRepository.findById(idTurno);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        TurnoEntity t = optionalEntity.get();
        Turno turno = modelMapper.map(t, Turno.class);
        if (t.getCliente() != null) {
            ClienteDTO clienteDTO = modelMapper.map(t.getCliente(), ClienteDTO.class);
            clienteDTO.setPersona(modelMapper.map(t.getCliente().getPersona(), PersonaDTO.class));
            if (t.getCliente().getPersona().getCiudad() != null)
                clienteDTO.getPersona().setCiudad(t.getCliente().getPersona().getCiudad().getDescripcion());
            if (t.getCliente().getPersona().getTipoDNI() != null)
                clienteDTO.getPersona().setTipoDNI(t.getCliente().getPersona().getTipoDNI().getDescripcion());
            clienteDTO.getPersona().setEmailUsuario(t.getCliente().getPersona().getUsuario().getEmail());
            turno.setCliente(clienteDTO);
        }

        ProfesionistaDTO profesionistaDTO = modelMapper.map(t.getProfesionista(),ProfesionistaDTO.class);
        profesionistaDTO.setPersona(modelMapper.map(t.getProfesionista().getPersona(), PersonaDTO.class));
        if(t.getProfesionista().getPersona().getCiudad() != null)
            profesionistaDTO.getPersona().setCiudad(t.getProfesionista().getPersona().getCiudad().getDescripcion());
        if(t.getProfesionista().getPersona().getTipoDNI() != null)
            profesionistaDTO.getPersona().setTipoDNI(t.getProfesionista().getPersona().getTipoDNI().getDescripcion());
        if(t.getProfesionista().getProfesiones() != null)
            mapearProfesiones(profesionistaDTO, t.getProfesionista());
        profesionistaDTO.getPersona().setEmailUsuario(t.getProfesionista().getPersona().getUsuario().getEmail());

        turno.setProfesionista(profesionistaDTO);

        return turno;
    }

    public MensajeRespuesta registrarTurno(TurnoDTOPost turnoDTOPost) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if (turnoJpaRepository.existsByCliente_IdAndProfesionista_Id(turnoDTOPost.getIdCliente(), turnoDTOPost.getIdProfesionista())){
                mensajeRespuesta.setMensaje("Esta persona ya tiene un turno asginado.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            TurnoEntity turnoEntity = new TurnoEntity(null, turnoDTOPost.getFechaTurno(), turnoDTOPost.getHoraTurno(), null,null);
            turnoEntity.setCliente(clienteJpaRepository.findById(turnoDTOPost.getIdCliente()).get());
            turnoEntity.setProfesionista(profesionistaJpaRepository.findById(turnoDTOPost.getIdProfesionista()).get());
            turnoJpaRepository.save(turnoEntity);

            mensajeRespuesta.setMensaje("Se ha realizado correctamente el guardado de turno.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al guardar el turno.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta modificarTurno(Long id, TurnoDTOPut turnoDTOPut) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            Optional<TurnoEntity> optionalEntity = turnoJpaRepository.findById(id);
            if(optionalEntity.isEmpty()){
                mensajeRespuesta.setMensaje("El turno a modificar no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            TurnoEntity turnoEntity =  optionalEntity.get();
            turnoEntity.setFechaTurno(turnoDTOPut.getFechaTurno());
            turnoEntity.setHoraTurno(turnoDTOPut.getHoraTurno());
            if(turnoDTOPut.getIdCliente() != null)
                turnoEntity.setCliente(clienteJpaRepository.findById(turnoDTOPut.getIdCliente()).get());
            else
                turnoEntity.setCliente(null);
            turnoEntity.setProfesionista(profesionistaJpaRepository.findById(turnoDTOPut.getIdProfesionista()).get());
            turnoJpaRepository.save(turnoEntity);
            mensajeRespuesta.setMensaje("Se ha modificado correctamente el turno.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al modificar el turno.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta borrarTurnoById(Long id) {
        try{
            if(turnoJpaRepository.existsById(id)){
                turnoJpaRepository.deleteById(id);
                return new MensajeRespuesta("Se ha eliminado correctamente el turno.", true);
            }else
                return new MensajeRespuesta("No se encontro el turno a eliminar.", false);
        }catch (Exception e){
            return new MensajeRespuesta("Problemas al eliminar el turno.", false);
        }
    }

    //Private

    private List<Turno> mapearListaTurnos(List<TurnoEntity> turnoEntities) {
        List<Turno> turnos = new ArrayList<>();
        for (TurnoEntity t : turnoEntities){
            Turno turno = modelMapper.map(t, Turno.class);
            if (t.getCliente() != null) {
                ClienteDTO clienteDTO = modelMapper.map(t.getCliente(), ClienteDTO.class);
                clienteDTO.setPersona(modelMapper.map(t.getCliente().getPersona(), PersonaDTO.class));
                if (t.getCliente().getPersona().getCiudad() != null)
                    clienteDTO.getPersona().setCiudad(t.getCliente().getPersona().getCiudad().getDescripcion());
                if (t.getCliente().getPersona().getTipoDNI() != null)
                    clienteDTO.getPersona().setTipoDNI(t.getCliente().getPersona().getTipoDNI().getDescripcion());
                clienteDTO.getPersona().setEmailUsuario(t.getCliente().getPersona().getUsuario().getEmail());
                turno.setCliente(clienteDTO);
            }

            ProfesionistaDTO profesionistaDTO = modelMapper.map(t.getProfesionista(),ProfesionistaDTO.class);
            profesionistaDTO.setPersona(modelMapper.map(t.getProfesionista().getPersona(), PersonaDTO.class));
            if(t.getProfesionista().getPersona().getCiudad() != null)
                profesionistaDTO.getPersona().setCiudad(t.getProfesionista().getPersona().getCiudad().getDescripcion());
            if(t.getProfesionista().getPersona().getTipoDNI() != null)
                profesionistaDTO.getPersona().setTipoDNI(t.getProfesionista().getPersona().getTipoDNI().getDescripcion());
            if(t.getProfesionista().getProfesiones() != null)
                mapearProfesiones(profesionistaDTO, t.getProfesionista());
            profesionistaDTO.getPersona().setEmailUsuario(t.getProfesionista().getPersona().getUsuario().getEmail());

            turno.setProfesionista(profesionistaDTO);
            turnos.add(turno);
        }
        return turnos;
    }


    public void mapearProfesiones(ProfesionistaDTO profesionistaDTO, ProfesionistaEntity profesionistaEntity) {
        List<String> profesionDTOList = new ArrayList<>();
        for (ProfesionEntity profesionEnt : profesionistaEntity.getProfesiones()) {
            profesionDTOList.add(profesionEnt.getDescripcion());
        }
        profesionistaDTO.setProfesiones(profesionDTOList);
    }
}
