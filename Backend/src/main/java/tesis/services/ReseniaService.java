package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.ReseniaDTOPost;
import tesis.dtos.ReseniaStats;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.Resenia;

import java.util.List;

@Service
public interface ReseniaService {
    List<Resenia> obtenerReseñas();
    List<Resenia> obtenerReseñasByCliente(Long idCliente);
    List<Resenia> obtenerReseñasByProfesionista(Long idProfesionista);
    Resenia obtenerReseñaById(Long idReseña);
    MensajeRespuesta registrarReseña(ReseniaDTOPost reseniaDTOPost);
    MensajeRespuesta borrarReseñaById(Long id);
    ReseniaStats estadisticasReseniasByProfesionista(Long idProfesionista);
}

