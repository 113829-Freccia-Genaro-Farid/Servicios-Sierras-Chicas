package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.LoginDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.UsuarioService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postLogin(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(usuarioService.loginUsuario(loginDTO));
    }

}
