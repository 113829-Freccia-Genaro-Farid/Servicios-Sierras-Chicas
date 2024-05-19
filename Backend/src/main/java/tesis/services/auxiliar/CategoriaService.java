package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.CategoriaDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Categoria;

import java.util.List;

@Service
public interface CategoriaService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(CategoriaDTO categoriaDTO);
    List<Categoria> obtenerCategorias();
}
