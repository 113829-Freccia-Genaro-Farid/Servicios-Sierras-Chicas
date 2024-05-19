package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ProfesionistaDTOPost;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.common.MensajeRespuesta;

import java.util.List;

@Service
public interface ProfesionistaService {
    List<ProfesionistaDTO> listarProfesionistas();
    ProfesionistaDTO obtenerProfesionistaById(Long id);
    MensajeRespuesta registrarProfesionista(ProfesionistaDTOPost profesionista);
    MensajeRespuesta modificarProfesionista(Long id, ProfesionistaDTOPut profesionista);
    MensajeRespuesta borrarProfesionistaById(Long id);
}
