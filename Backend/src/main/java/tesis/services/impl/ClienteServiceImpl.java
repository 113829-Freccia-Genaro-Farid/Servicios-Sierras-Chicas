package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ClienteDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.ClienteEntity;
import tesis.entities.PersonaEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.ClienteJpaRepository;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.ClienteService;
import tesis.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteJpaRepository clienteJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<ClienteDTO> listarClientes() {
        List<ClienteDTO> lst;
        try{
            List<ClienteEntity> lista= clienteJpaRepository.findAll();
            lst = mapearListaClientes(lista);
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public ClienteDTO obtenerClienteById(Long id) {
        try {
            Optional<ClienteEntity> optionalEntity = clienteJpaRepository.findById(id);
            if (optionalEntity.isPresent()) {
                ClienteEntity entity = optionalEntity.get();
                ClienteDTO clienteDTO = modelMapper.map(entity, ClienteDTO.class);
                if(clienteDTO.getPersona().getCiudad() != null)
                    clienteDTO.getPersona().setCiudad(entity.getPersona().getCiudad().getDescripcion());
                if(clienteDTO.getPersona().getTipoDNI() != null)
                    clienteDTO.getPersona().setTipoDNI(entity.getPersona().getTipoDNI().getDescripcion());
                clienteDTO.getPersona().setEmailUsuario(entity.getPersona().getUsuario().getEmail());
                return clienteDTO;
            }
            else
                return null;
        }catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
    }

    @Override
    @Transactional
    public MensajeRespuesta registrarCliente(ClienteDTOPost cliente) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            if(personaJpaRepository.findById(cliente.getIdPersona()).isEmpty()){
                mensajeRespuesta.setMensaje("La persona a registrarse como cliente no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if(!personaJpaRepository.findById(cliente.getIdPersona()).get().isHabilitado()){
                mensajeRespuesta.setMensaje("No puede registrarse ya que tiene datos personales incompletos.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if (clienteJpaRepository.existsByPersona_Id(cliente.getIdPersona())){
                mensajeRespuesta.setMensaje("Esta persona ya se encuentra registrada como cliente.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ClienteEntity clienteEntity = new ClienteEntity();
            clienteEntity.setPersona(personaJpaRepository.findById(cliente.getIdPersona()).get());
            usuarioService.cambiarRolUsuario(clienteEntity.getPersona().getUsuario().getEmail(), rolJpaRepository.getByDescripcion("CLIENTE").getId());

            clienteJpaRepository.save(clienteEntity);
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al guardar al cliente.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta modificarCliente(Long id, ClienteDTOPost cliente) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try{
            Optional<ClienteEntity> optionalEntity = clienteJpaRepository.findById(id);
            if(optionalEntity.isEmpty()){
                mensajeRespuesta.setMensaje("El cliente a modificar no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if(personaJpaRepository.findById(cliente.getIdPersona()).isEmpty()){
                mensajeRespuesta.setMensaje("La persona a registrarse como cliente no existe.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            if (clienteJpaRepository.existsByPersona_Id(cliente.getIdPersona())){
                mensajeRespuesta.setMensaje("Esta persona ya se encuentra registrada como cliente.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            ClienteEntity clienteEntity =  optionalEntity.get();
            clienteEntity.setPersona(personaJpaRepository.findById(cliente.getIdPersona()).get());

            clienteJpaRepository.save(clienteEntity);
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al modificar al cliente.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    // PRIVADOS

    private List<ClienteDTO> mapearListaClientes(List<ClienteEntity> clienteEntities) {
        List<ClienteDTO> clienteDTOS = new ArrayList<>();
        for (ClienteEntity c : clienteEntities){
            ClienteDTO clienteDTO = modelMapper.map(c,ClienteDTO.class);
            if(c.getPersona().getCiudad() != null)
                clienteDTO.getPersona().setCiudad(c.getPersona().getCiudad().getDescripcion());
            if(c.getPersona().getTipoDNI() != null)
                clienteDTO.getPersona().setTipoDNI(c.getPersona().getTipoDNI().getDescripcion());
            clienteDTO.getPersona().setEmailUsuario(c.getPersona().getUsuario().getEmail());
            clienteDTOS.add(clienteDTO);
        }
        return clienteDTOS;
    }
}
