package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.TurnoDTOPost;
import tesis.dtos.TurnoDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.Turno;

import java.util.List;

@Service
public interface TurnoService {
    List<Turno> obtenerTurnos();

    List<Turno> obtenerTurnosByCliente(Long idCliente);

    List<Turno> obtenerTurnosByProfesionista(Long idProfesionista);

    Turno obtenerTurnoById(Long idTurno);

    MensajeRespuesta registrarTurno(TurnoDTOPost turnoDTOPost);
    MensajeRespuesta modificarTurno(Long id, TurnoDTOPut turnoDTOPut);

    MensajeRespuesta borrarTurnoById(Long id);
}
