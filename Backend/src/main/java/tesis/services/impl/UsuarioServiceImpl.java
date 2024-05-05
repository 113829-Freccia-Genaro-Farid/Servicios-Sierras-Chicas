package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tesis.dtos.LoginDTO;
import tesis.dtos.UsuarioDTO;
import tesis.dtos.UsuarioDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.PersonaEntity;
import tesis.entities.UsuarioEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.PersonaJpaRepository;
import tesis.repositories.UsuarioJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.UsuarioService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UsuarioDTO> listarUsuarios(){
        List<UsuarioDTO> lst = new ArrayList<>();
        try{
            List<UsuarioEntity> lista= usuarioJpaRepository.findAll();
            for (UsuarioEntity u:lista){
                lst.add(new UsuarioDTO(u.getEmail(),
                        u.isActivo(),
                        u.getFechaAlta(),
                        (u.getRol() != null) ? u.getRol().getDescripcion() : null));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<UsuarioDTO> listarUsuariosFiltro(String email){
        List<UsuarioDTO> lst = new ArrayList<>();
        try{
            List<UsuarioEntity> lista = usuarioJpaRepository.findByEmailStartingWith(email);
            for (UsuarioEntity u:lista){
                lst.add(new UsuarioDTO(u.getEmail(),
                        u.isActivo(),
                        u.getFechaAlta(),
                        (u.getRol() != null) ? u.getRol().getDescripcion() : null));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public List<UsuarioDTO> listarUsuariosByRol(Long idRol) {
        List<UsuarioDTO> lst = new ArrayList<>();
        try{
            List<UsuarioEntity> lista = usuarioJpaRepository.findByRol_Id(idRol);
            for (UsuarioEntity u:lista){
                lst.add(new UsuarioDTO(u.getEmail(),
                        u.isActivo(),
                        u.getFechaAlta(),
                        (u.getRol() != null) ? u.getRol().getDescripcion() : null));
            }
        }catch (Exception e){
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return lst;
    }

    @Override
    public UsuarioDTO obtenerUsuarioByEmail(String email) {
        try{
            UsuarioEntity entity = usuarioJpaRepository.getByEmail(email.toLowerCase());
            if (entity != null)
                return new UsuarioDTO(entity.getEmail(),
                        entity.isActivo(),
                        entity.getFechaAlta(),
                        (entity.getRol() != null) ? entity.getRol().getDescripcion() : null);
            else
                return null;
        }catch (Exception e) {
            MensajeRespuesta mensajeRespuesta = new MensajeRespuesta("Error interno",false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
    }

    @Override
    @Transactional
    public MensajeRespuesta registrarUsuario(UsuarioDTOPost usuarioDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        // SE PASA A MINUSCULAS PARA TENER ESTANDARIZADO
        usuarioDTO.setEmail(usuarioDTO.getEmail().toLowerCase());
        try{
            if(usuarioJpaRepository.existsByEmail(usuarioDTO.getEmail())){
                mensajeRespuesta.setMensaje("Ya existe un usuario registrado con ese email.");
                mensajeRespuesta.setOk(false);
                return mensajeRespuesta;
            }
            UsuarioEntity entity = modelMapper.map(usuarioDTO, UsuarioEntity.class);
            entity.setRol(null);
            usuarioJpaRepository.save(entity);
            personaJpaRepository.save(new PersonaEntity(null,"","",null, LocalDate.now(),null,"","","","",null,null,entity.getFechaAlta(),false,entity));
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al grabar el usuario.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta bajaUsuario(String emailBajaUsuario) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        try{
            UsuarioEntity entity = usuarioJpaRepository.getByEmail(emailBajaUsuario);
            if (entity != null){
                entity.setActivo(false);
                usuarioJpaRepository.save(entity);
            }else{
                mensajeRespuesta.setMensaje("No se encontro el usuario con el mail "+emailBajaUsuario+".");
                mensajeRespuesta.setOk(false);
            }

        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al dar de baja al usuario.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }

        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta cambiarRolUsuario(String email, Long idRol) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        try{
            UsuarioEntity entity = usuarioJpaRepository.getByEmail(email);
            if (entity != null){
                if(idRol != null){
                    entity.setRol(rolJpaRepository.findById(idRol).get());
                    usuarioJpaRepository.save(entity);
                }else {
                    entity.setRol(null);
                    usuarioJpaRepository.save(entity);
                }
            }else{
                mensajeRespuesta.setMensaje("No se encontro el usuario con el mail "+email+".");
                mensajeRespuesta.setOk(false);
            }

        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al cambiar de rol al usuario.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta loginUsuario(LoginDTO loginDTO) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        // TODO: VER SI INGRESA ESTANDO INACTIVO O NO
        loginDTO.setEmail(loginDTO.getEmail().toLowerCase());
        try{
            if (!usuarioJpaRepository.existsByEmail(loginDTO.getEmail())) {
                mensajeRespuesta.setOk(false);
                mensajeRespuesta.setMensaje("El email ingresado no esta registrado.");

            }else if (!usuarioJpaRepository.existsByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword())) {
                mensajeRespuesta.setOk(false);
                mensajeRespuesta.setMensaje("La contraseña ingresada es incorrecta.");
            }
        }catch (Exception e){
            mensajeRespuesta.setOk(false);
            mensajeRespuesta.setMensaje("Error al realizar el login.");
            throw new MensajeRespuestaException(mensajeRespuesta);
        }

        return mensajeRespuesta;
    }
}
