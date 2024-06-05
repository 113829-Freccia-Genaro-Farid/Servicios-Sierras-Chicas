package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.UsuarioDTO;
import tesis.dtos.UsuarioDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.UsuarioService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("")
    public ResponseEntity<List<UsuarioDTO>> getUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
    @GetMapping("/filtro")
    public ResponseEntity<List<UsuarioDTO>> getUsuarios(@RequestParam(name = "email") String email){
        return ResponseEntity.ok(usuarioService.listarUsuariosFiltro(email));
    }
    @GetMapping("/rol")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByRol(@RequestParam(name = "idRol", required = false) Long idRol){
        return ResponseEntity.ok(usuarioService.listarUsuariosByRol(idRol));
    }
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@PathVariable(name = "email") String email){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioByEmail(email));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postUsuario(@RequestBody UsuarioDTOPost usuarioDTOPost){
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuarioDTOPost));
    }
    @DeleteMapping("/{email}")
    public ResponseEntity<MensajeRespuesta> bajaUsuario(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.bajaUsuario(email));
    }
    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<MensajeRespuesta> borrarUsuarioSistema(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.eliminarUsuarioSistema(email));
    }
    @PutMapping("/cambioRol/{email}")
    public ResponseEntity<MensajeRespuesta> cambiarRolUsuario(@PathVariable String email, @RequestParam(required = false) Long idRol){
        return ResponseEntity.ok(usuarioService.cambiarRolUsuario(email, idRol));
    }
}
