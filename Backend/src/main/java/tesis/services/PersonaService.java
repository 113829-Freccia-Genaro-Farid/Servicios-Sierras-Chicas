package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.PersonaDTO;
import tesis.dtos.PersonaDTOPut;
import tesis.dtos.common.MensajeRespuesta;

import java.util.List;

@Service
public interface PersonaService {
    List<PersonaDTO> listarPersonas();
    List<PersonaDTO> listarPersonasFiltro(String apellido);
    PersonaDTO obtenerPersonaById(Long id);
    MensajeRespuesta modificarPersona(PersonaDTOPut persona, Long id);
}
