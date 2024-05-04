package tesis.services.auxiliar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.RolDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.auxiliar.RolEntity;
import tesis.entities.auxiliar.TipoDNIEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.models.auxiliar.Rol;
import tesis.models.auxiliar.TipoDNI;
import tesis.repositories.auxiliar.RolJpaRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class RolServiceImpl implements RolService{
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean borrarById(Long id) {
        try{
            if(rolJpaRepository.existsById(id)){
                rolJpaRepository.deleteById(id);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public MensajeRespuesta registrar(RolDTO rolDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(rolJpaRepository.existsByDescripcion(rolDTO.getDescripcion())){
                mensajeRespuesta.setMensaje("Ya existe un rol con el mismo nombre.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            rolJpaRepository.save(modelMapper.map(rolDTO, RolEntity.class));
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar el rol.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public List<Rol> obtenerRoles() {
        List<Rol> lst = new ArrayList<>();
        try{
            List<RolEntity> lista = rolJpaRepository.findAll();
            for (RolEntity rolEntity:lista){
                lst.add(modelMapper.map(rolEntity,Rol.class));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }
}
