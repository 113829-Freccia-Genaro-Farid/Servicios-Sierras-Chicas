package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.CiudadDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Ciudad;

import java.util.List;

@Service
public interface CiudadService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(CiudadDTO ciudadDTO);
    List<Ciudad> obtenerCiudadesPorProvincia(Long idProvincia);
}
