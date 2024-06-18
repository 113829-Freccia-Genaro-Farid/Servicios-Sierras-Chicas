package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.ProfesionDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.ProfesionistaEntity;
import tesis.entities.auxiliar.ProfesionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Categoria;
import tesis.models.auxiliar.Profesion;
import tesis.repositories.ProfesionistaJpaRepository;
import tesis.repositories.auxiliar.CategoriaJpaRepository;
import tesis.repositories.auxiliar.ProfesionJpaRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProfesionServiceImpl implements ProfesionService{
    @Autowired
    ProfesionJpaRepository profesionJpaRepository;
    @Autowired
    CategoriaJpaRepository categoriaJpaRepository;
    @Autowired
    ProfesionistaJpaRepository profesionistaJpaRepository;
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
            List<ProfesionEntity> lista = profesionJpaRepository.getAllByCategoria_IdOrderByDescripcion(idCategoria);
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

    @Override
    public List<Profesion> obtenerProfesionesPorCategoriaEnUso(Long idCategoria) {
        List<Profesion> lst = new ArrayList<>();
        try {
            List<ProfesionistaEntity> lista = profesionistaJpaRepository.findAll();
            List<ProfesionEntity> listaProf = profesionJpaRepository.getAllByCategoria_IdOrderByDescripcion(idCategoria);
            for (ProfesionistaEntity p : lista) {
                for (ProfesionEntity pr : p.getProfesiones()) {
                    if (listaProf.contains(pr)) {
                        Profesion profesion = modelMapper.map(pr, Profesion.class);
                        if (!lst.contains(profesion)) {
                            lst.add(profesion);
                        }
                    }
                }
            }
            lst.sort(Comparator.comparing(Profesion::getDescripcion));
        } catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno", false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }


    @Override
    public List<Profesion> obtenerProfesiones() {
        List<Profesion> lst = new ArrayList<>();
        try{
            List<ProfesionEntity> lista = profesionJpaRepository.findAllByOrderByDescripcion();
            for (ProfesionEntity p:lista){
                Profesion profesion = modelMapper.map(p,Profesion.class);
                profesion.setIdCategoria(p.getCategoria().getId());
                lst.add(profesion);
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<Profesion> obtenerProfesionesEnUso() {
        List<Profesion> lst = new ArrayList<>();
        try {
            List<ProfesionistaEntity> lista = profesionistaJpaRepository.findAll();
            for (ProfesionistaEntity p : lista) {
                for (ProfesionEntity pr : p.getProfesiones()) {
                    Profesion profesion = modelMapper.map(pr, Profesion.class);
                    if (!lst.contains(profesion)) {
                        lst.add(profesion);
                    }
                }
            }
            lst.sort(Comparator.comparing(Profesion::getDescripcion));
        } catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno", false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
