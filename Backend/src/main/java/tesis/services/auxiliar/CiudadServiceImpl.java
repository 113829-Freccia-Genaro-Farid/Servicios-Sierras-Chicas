package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.CiudadDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.CiudadEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Ciudad;
import tesis.repositories.auxiliar.CiudadJpaRepository;
import tesis.repositories.auxiliar.ProvinciaJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CiudadServiceImpl implements CiudadService{
    @Autowired
    CiudadJpaRepository ciudadJpaRepository;
    @Autowired
    ProvinciaJpaRepository provinciaJpaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean borrarById(Long id) {
        try{
            if(ciudadJpaRepository.existsById(id)){
                ciudadJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public MensajeRespuesta registrar(CiudadDTO ciudadDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(ciudadJpaRepository.existsByDescripcion(ciudadDTO.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe una ciudad con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            CiudadEntity ciudadEntity = new CiudadEntity(null, ciudadDTO.getDescripcion(), ciudadDTO.getCodigoPostal(), provinciaJpaRepository.findById(ciudadDTO.getId_provincia()).get());
            ciudadJpaRepository.save(ciudadEntity);
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar la ciudad.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<Ciudad> obtenerCiudadesPorProvincia(Long idProvincia) {
        List<Ciudad> lst = new ArrayList<>();
        try{
            List<CiudadEntity> lista = ciudadJpaRepository.getAllByProvincia_Id(idProvincia);
            for (CiudadEntity c:lista){
                Ciudad ciudad = modelMapper.map(c,Ciudad.class);
                ciudad.setId_provincia(idProvincia);
                lst.add(ciudad);
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
