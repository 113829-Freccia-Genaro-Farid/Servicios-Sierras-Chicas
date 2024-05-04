package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.TipoDNIDto;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.TipoDNIEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.TipoDNI;
import tesis.repositories.auxiliar.TipoDNIJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TipoDNIServiceImpl implements TipoDNIService {
    @Autowired
    TipoDNIJpaRepository tipoDNIJpaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean borrarById(Long id) {
        try{
            if(tipoDNIJpaRepository.existsById(id)){
                tipoDNIJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public MensajeRespuesta registrar(TipoDNIDto tipoDNIDto) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(tipoDNIJpaRepository.existsByDescripcion(tipoDNIDto.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe un tipo con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            tipoDNIJpaRepository.save(modelMapper.map(tipoDNIDto, TipoDNIEntity.class));
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar el tipo.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<TipoDNI> obtenerTipos() {
        List<TipoDNI> lst = new ArrayList<>();
        try{
            List<TipoDNIEntity> lista = tipoDNIJpaRepository.findAll();
            for (TipoDNIEntity tipoDNIEntity:lista){
                lst.add(modelMapper.map(tipoDNIEntity,TipoDNI.class));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
