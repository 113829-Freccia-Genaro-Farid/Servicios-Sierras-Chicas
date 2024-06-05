package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.TurnoDTOPost;
import tesis.dtos.TurnoDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.Turno;
import tesis.services.TurnoService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/turnos")
public class TurnoController {
    @Autowired
    TurnoService turnoService;

    @GetMapping("")
    public ResponseEntity<List<Turno>> getTurnos(){
        return ResponseEntity.ok(turnoService.obtenerTurnos());
    }
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Turno>> getTurnoByCliente(@PathVariable Long idCliente){
        return ResponseEntity.ok(turnoService.obtenerTurnosByCliente(idCliente));
    }
    @GetMapping("/profesionista/{idProfesionista}")
    public ResponseEntity<List<Turno>> getTurnoByProfesionista(@PathVariable Long idProfesionista){
        return ResponseEntity.ok(turnoService.obtenerTurnosByProfesionista(idProfesionista));
    }
    @GetMapping("/{idturno}")
    public ResponseEntity<Turno> getTurnoById(@PathVariable Long idturno){
        return ResponseEntity.ok(turnoService.obtenerTurnoById(idturno));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postTurno(@RequestBody TurnoDTOPost turnoDTOPost){
        return ResponseEntity.ok(turnoService.registrarTurno(turnoDTOPost));
    }
    @PutMapping("/{idTurno}")
    public ResponseEntity<MensajeRespuesta> putTurno(@PathVariable Long idTurno, @RequestBody TurnoDTOPut turnoDTOPut){
        return ResponseEntity.ok(turnoService.modificarTurno(idTurno, turnoDTOPut));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> deleteTurno(@PathVariable Long id){
        return ResponseEntity.ok(turnoService.borrarTurnoById(id));
    }
}
