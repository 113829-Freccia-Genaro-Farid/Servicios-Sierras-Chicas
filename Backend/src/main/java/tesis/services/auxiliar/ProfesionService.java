package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.ProfesionDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Profesion;

import java.util.List;

@Service
public interface ProfesionService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(ProfesionDTO profesionDTO);
    List<Profesion> obtenerProfesionesPorCategoria(Long idCategoria);
    List<Profesion> obtenerProfesionesPorCategoriaEnUso(Long idCategoria);
    List<Profesion> obtenerProfesiones();
    List<Profesion> obtenerProfesionesEnUso();
}
