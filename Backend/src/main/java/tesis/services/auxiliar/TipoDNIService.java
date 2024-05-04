package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.TipoDNIDto;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.TipoDNI;

import java.util.List;

@Service
public interface TipoDNIService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(TipoDNIDto tipoDNIDto);
    List<TipoDNI> obtenerTipos();
}
