package tesis.services;

import org.springframework.stereotype.Service;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ClienteDTOPost;
import tesis.dtos.common.MensajeRespuesta;

import java.util.List;

@Service
public interface ClienteService {
    List<ClienteDTO> listarClientes();
    ClienteDTO obtenerClienteById(Long id);
    MensajeRespuesta registrarCliente(ClienteDTOPost cliente);

    //MensajeRespuesta modificarCliente(Long id, ClienteDTOPut cliente);
}
