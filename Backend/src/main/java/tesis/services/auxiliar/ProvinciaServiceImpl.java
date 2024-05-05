package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.ProvinciaDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.ProvinciaEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Provincia;
import tesis.repositories.auxiliar.ProvinciaJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinciaServiceImpl implements ProvinciaService{
    @Autowired
    ProvinciaJpaRepository provinciaJpaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean borrarById(Long id) {
        try{
            if(provinciaJpaRepository.existsById(id)){
                provinciaJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public MensajeRespuesta registrar(ProvinciaDTO provinciaDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(provinciaJpaRepository.existsByDescripcion(provinciaDTO.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe una provincia con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            provinciaJpaRepository.save(modelMapper.map(provinciaDTO, ProvinciaEntity.class));
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar la provincia.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<Provincia> obtenerProvincias() {
        List<Provincia> lst = new ArrayList<>();
        try{
            List<ProvinciaEntity> lista = provinciaJpaRepository.findAll();
            for (ProvinciaEntity p:lista){
                lst.add(modelMapper.map(p,Provincia.class));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
