package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.ProvinciaDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Provincia;

import java.util.List;

@Service
public interface ProvinciaService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(ProvinciaDTO provinciaDTO);
    List<Provincia> obtenerProvincias();
}
