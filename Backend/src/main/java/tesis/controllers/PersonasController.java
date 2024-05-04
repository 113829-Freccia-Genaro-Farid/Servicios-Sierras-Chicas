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
public class PersonasController {
    @Autowired
    PersonaService personaService;
    @GetMapping("")
    public ResponseEntity<List<PersonaDTO>> getPersonas(){
        return ResponseEntity.ok(personaService.listarPersonas());
    }
    @GetMapping("/filtro")
    public ResponseEntity<List<PersonaDTO>> getPersonas(@RequestParam(required = false) String apellido){
        return ResponseEntity.ok(personaService.listarPersonasFiltro(apellido));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> getPersonaById(@PathVariable Long id){
        return ResponseEntity.ok(personaService.obtenerPersonaById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> postPersona(@RequestBody PersonaDTOPut personaDTOPut, @PathVariable Long id){
        return ResponseEntity.ok(personaService.modificarPersona(personaDTOPut, id));
    }
}
