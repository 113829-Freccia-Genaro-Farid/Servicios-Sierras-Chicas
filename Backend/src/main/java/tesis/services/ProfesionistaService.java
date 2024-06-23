package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.AnuncioDTO;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ProfesionistaDTOPost;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Categoria;

import java.util.List;

@Service
public interface ProfesionistaService {
    List<ProfesionistaDTO> listarProfesionistas();
    List<ProfesionistaDTO> listarProfesionistasOrdenadaSuscritos();
    List<ProfesionistaDTO> listaProfesionistasConFiltros(String nombreApellido, List<Long> categorias, List<Long> profesiones, List<Long> ciudades);
    ProfesionistaDTO obtenerProfesionistaById(Long id);
    ProfesionistaDTO obtenerProfesionistaByEmailUser(String email);
    MensajeRespuesta registrarProfesionista(ProfesionistaDTOPost profesionista);
    MensajeRespuesta modificarProfesionista(Long id, ProfesionistaDTOPut profesionista);
    MensajeRespuesta modificarPresentacionProfesionista(Long id, String nuevaPresentacion);
    MensajeRespuesta borrarProfesionistaById(Long id);

    // **** ANUNCIOS ****
    MensajeRespuesta clickEnAnuncio(Long idProfesionista);
    List<AnuncioDTO> getAnuncios();
    List<AnuncioDTO> getAnunciosByProfesionista(Long idProfesionista);
}
