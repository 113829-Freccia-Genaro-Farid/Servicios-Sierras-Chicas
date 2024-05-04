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
    public ResponseEntity<List<UsuarioDTO>> getUsuarios(@RequestParam(name = "email", required = false) String email){
        return ResponseEntity.ok(usuarioService.listarUsuariosFiltro(email));
    }
    @GetMapping("/usuario")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@RequestParam(name = "email") String email){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioByEmail(email));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postUsuario(@RequestBody UsuarioDTOPost usuarioDTOPost){
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuarioDTOPost));
    }
    @PutMapping("")
    public ResponseEntity<MensajeRespuesta> bajaUsuario(@RequestParam String emailBajaUsuario){
        return ResponseEntity.ok(usuarioService.bajaUsuario(emailBajaUsuario));
    }
}
