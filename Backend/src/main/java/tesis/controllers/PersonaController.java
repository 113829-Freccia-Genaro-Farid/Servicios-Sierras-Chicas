package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.PersonaDTO;
import tesis.dtos.PersonaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.PersonaService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    PersonaService personaService;
    @GetMapping("")
    public ResponseEntity<List<PersonaDTO>> getPersonas(){
        return ResponseEntity.ok(personaService.listarPersonasHabilitadas());
    }
    @GetMapping("/filterByApellido")
    public ResponseEntity<List<PersonaDTO>> getPersonasFiltrado(@RequestParam(required = false) String apellido){
        return ResponseEntity.ok(personaService.listarPersonasFiltro(apellido));
    }
    @GetMapping("/id/{idPersona}")
    public ResponseEntity<PersonaDTO> getPersonaById(@PathVariable Long idPersona){
        return ResponseEntity.ok(personaService.obtenerPersonaById(idPersona));
    }
    @GetMapping("/{email}")
    public ResponseEntity<PersonaDTO> getPersonaById(@PathVariable String email){
        return ResponseEntity.ok(personaService.obtenerPersonaByUser(email));
    }
    @PutMapping("/{idPersona}")
    public ResponseEntity<MensajeRespuesta> putPersona(@RequestBody PersonaDTOPut personaDTOPut, @PathVariable Long idPersona){
        return ResponseEntity.ok(personaService.modificarPersona(personaDTOPut, idPersona));
    }
}
