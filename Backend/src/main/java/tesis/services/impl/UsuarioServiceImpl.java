package tesis.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tesis.dtos.LoginDTO;
import tesis.dtos.UsuarioDTO;
import tesis.dtos.UsuarioDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.entities.*;
import tesis.entities.auxiliar.RecuperacionEntity;
import tesis.exceptions.MensajeRespuestaException;
import tesis.repositories.*;
import tesis.repositories.auxiliar.RecuperacionJpaRepository;
import tesis.repositories.auxiliar.RolJpaRepository;
import tesis.services.UsuarioService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    RolJpaRepository rolJpaRepository;
    @Autowired
    PersonaJpaRepository personaJpaRepository;
    @Autowired
    ProfesionistaJpaRepository profesionistaJpaRepository;
    @Autowired
    ClienteJpaRepository clienteJpaRepository;
    @Autowired
    TurnoJpaRepository turnoJpaRepository;
    @Autowired
    AnuncioJpaRepository anuncioJpaRepository;
    @Autowired
    ReseniaJpaRepository reseniaJpaRepository;
    @Autowired
    RecuperacionJpaRepository recuperacionJpaRepository;
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
            List<UsuarioEntity> lista = usuarioJpaRepository.findByEmailStartingWith(email.toLowerCase());
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
            UsuarioEntity entity = new UsuarioEntity(usuarioDTO.getEmail(), usuarioDTO.getPassword(), true, LocalDate.now(), null);
            usuarioJpaRepository.save(entity);
            mensajeRespuesta.setMensaje("Usuario registrado con exito.");
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
        emailBajaUsuario = emailBajaUsuario.toLowerCase();
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
        email = email.toLowerCase();
        try{
            UsuarioEntity entity = usuarioJpaRepository.getByEmail(email);
            if (entity != null){
                if(idRol != null && rolJpaRepository.findById(idRol).isPresent() ){
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
            if (mensajeRespuesta.isOk()) {
                mensajeRespuesta.setMensaje("Login realizado con exito.");
            }
        }catch (Exception e){
            mensajeRespuesta.setOk(false);
            mensajeRespuesta.setMensaje("Error al realizar el login.");
            throw new MensajeRespuestaException(mensajeRespuesta);
        }

        return mensajeRespuesta;
    }

    @Override
    @Transactional
    public MensajeRespuesta eliminarUsuarioSistema(String emailUsuario) {
        try{
            if(usuarioJpaRepository.existsByEmail(emailUsuario)){
                if(personaJpaRepository.existsByUsuario_Email(emailUsuario)) {
                    PersonaEntity persona = personaJpaRepository.findByUsuarioEmail(emailUsuario);
                    if (profesionistaJpaRepository.existsByPersona_Id(persona.getId())){
                        for (AnuncioEntity e : anuncioJpaRepository.findAllByProfesionista_Id(profesionistaJpaRepository.getByPersona_Id(persona.getId()).getId())) {
                            anuncioJpaRepository.deleteById(e.getId());
                        }
                        for (TurnoEntity t : turnoJpaRepository.findAllByProfesionista_Id(profesionistaJpaRepository.getByPersona_Id(persona.getId()).getId())) {
                            turnoJpaRepository.deleteById(t.getId());
                        }
                        for (ReseniaEntity r : reseniaJpaRepository.findAllByProfesionista_Id(profesionistaJpaRepository.getByPersona_Id(persona.getId()).getId())) {
                            reseniaJpaRepository.deleteById(r.getId());
                        }
                        profesionistaJpaRepository.deleteById(profesionistaJpaRepository.getByPersona_Id(persona.getId()).getId());
                    }else if (clienteJpaRepository.existsByPersona_Id(persona.getId())){
                        for (TurnoEntity t : turnoJpaRepository.findAllByCliente_Id(clienteJpaRepository.getByPersona_Id(persona.getId()).getId())) {
                            turnoJpaRepository.deleteById(t.getId());
                        }
                        for (ReseniaEntity r : reseniaJpaRepository.findAllByProfesionista_Id(clienteJpaRepository.getByPersona_Id(persona.getId()).getId())) {
                            r.setCliente(null);
                            reseniaJpaRepository.save(r);
                        }
                        clienteJpaRepository.deleteById(clienteJpaRepository.getByPersona_Id(persona.getId()).getId());
                    }
                    personaJpaRepository.deleteById(persona.getId());
                }
                usuarioJpaRepository.deleteByEmail(emailUsuario);
                return new MensajeRespuesta("Se ha eliminado correctamente al usuario de todo el sistema.", true);
            }else
                return new MensajeRespuesta("No se encontro al usuario a eliminar.", false);
        }catch (Exception e){
            return new MensajeRespuesta("Problemas al eliminar al usuario.", false);
        }
    }

    @Override
    public MensajeRespuesta cambiarContraseña(String email, String nuevaPass) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        email = email.toLowerCase();
        try{
            UsuarioEntity entity = usuarioJpaRepository.getByEmail(email);
            if (entity != null){
                if(!nuevaPass.equals(entity.getPassword())){
                    entity.setPassword(nuevaPass);
                    usuarioJpaRepository.save(entity);
                    mensajeRespuesta.setMensaje("La constraseña se cambio con exito.");
                }else {
                    mensajeRespuesta.setMensaje("La nueva contraseña ingresada es identica a la anterior.");
                    mensajeRespuesta.setOk(false);
                }
            }else{
                mensajeRespuesta.setMensaje("No se encontro usuario registrado con ese email.");
                mensajeRespuesta.setOk(false);
            }

        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al cambiar de contraseña.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta verificacionCodigo(String email, String codigo) {
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        email = email.toLowerCase();
        Optional<RecuperacionEntity> entity = recuperacionJpaRepository.findFirstByEmailOrderByFechaSolicitudDesc(email);
        try{
            if (entity.isPresent()){
                if(entity.get().getCodigoRecuperacion().equals(codigo)){
                    mensajeRespuesta.setMensaje("Codigo correcto.");
                }else {
                    mensajeRespuesta.setMensaje("El codigo de verificacion es incorrecto.");
                    mensajeRespuesta.setOk(false);
                }
            }else{
                mensajeRespuesta.setMensaje("No se encontro usuario registrado con ese email.");
                mensajeRespuesta.setOk(false);
            }
        }catch (Exception e){
            mensajeRespuesta.setMensaje("Error al verificar codigo.");
            mensajeRespuesta.setOk(false);
            throw new MensajeRespuestaException(mensajeRespuesta);
        }
        return mensajeRespuesta;
    }
}
