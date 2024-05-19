package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.ProfesionDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Profesion;
import tesis.repositories.auxiliar.CategoriaJpaRepository;
import tesis.repositories.auxiliar.ProfesionJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesionServiceImpl implements ProfesionService{
    @Autowired
    ProfesionJpaRepository profesionJpaRepository;
    @Autowired
    CategoriaJpaRepository categoriaJpaRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean borrarById(Long id) {
        try{
            if(profesionJpaRepository.existsById(id)){
                profesionJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public MensajeRespuesta registrar(ProfesionDTO profesionDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(profesionJpaRepository.existsByDescripcion(profesionDTO.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe una profesion con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ProfesionEntity profesionEntity = new ProfesionEntity(null, profesionDTO.getDescripcion(), categoriaJpaRepository.findById(profesionDTO.getIdCategoria()).get());
            profesionJpaRepository.save(profesionEntity);
            mensajeRespuesta.setMensaje("Se ha guardado correctamente la profesion.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar la profesion.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<Profesion> obtenerProfesionesPorCategoria(Long idCategoria) {
        List<Profesion> lst = new ArrayList<>();
        try{
            List<ProfesionEntity> lista = profesionJpaRepository.getAllByCategoria_Id(idCategoria);
            for (ProfesionEntity p:lista){
                Profesion profesion = modelMapper.map(p,Profesion.class);
                profesion.setIdCategoria(idCategoria);
                lst.add(profesion);
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
