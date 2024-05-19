package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.CategoriaDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.CategoriaEntity;
import tesis.entities.auxiliar.ProvinciaEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Categoria;
import tesis.models.auxiliar.Provincia;
import tesis.repositories.auxiliar.CategoriaJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    CategoriaJpaRepository categoriaJpaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean borrarById(Long id) {
        try{
            if(categoriaJpaRepository.existsById(id)){
                categoriaJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public MensajeRespuesta registrar(CategoriaDTO categoriaDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(categoriaJpaRepository.existsByDescripcion(categoriaDTO.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe una categoria con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            categoriaJpaRepository.save(modelMapper.map(categoriaDTO, CategoriaEntity.class));
            mensajeRespuesta.setMensaje("Se ha guardado correctamente la categoria.");
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar la categoria.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        List<Categoria> lst = new ArrayList<>();
        try{
            List<CategoriaEntity> lista = categoriaJpaRepository.findAll();
            for (CategoriaEntity c:lista){
                lst.add(modelMapper.map(c,Categoria.class));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
