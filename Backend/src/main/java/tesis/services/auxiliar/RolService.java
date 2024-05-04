package tesis.services.auxiliar;

import org.springframework.stereotype.Service;
import tesis.dtos.auxiliar.RolDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Rol;

import java.util.List;

@Service
public interface RolService {
    boolean borrarById(Long id);
    MensajeRespuesta registrar(RolDTO rolDTO);
    List<Rol> obtenerRoles();
}
