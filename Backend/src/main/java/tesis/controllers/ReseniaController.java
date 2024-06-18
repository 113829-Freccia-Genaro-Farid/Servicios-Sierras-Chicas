package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.ReseniaDTOPost;
import tesis.dtos.ReseniaStats;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.Resenia;
import tesis.services.ReseniaService;
import tesis.services.TurnoService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/resenas")
public class ReseniaController {
    @Autowired
    ReseniaService reseniaService;

    @GetMapping("")
    public ResponseEntity<List<Resenia>> getReseñas(){
        return ResponseEntity.ok(reseniaService.obtenerReseñas());
    }
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Resenia>> getReseñaByCliente(@PathVariable Long idCliente){
        return ResponseEntity.ok(reseniaService.obtenerReseñasByCliente(idCliente));
    }
    @GetMapping("/profesionista/{idProfesionista}")
    public ResponseEntity<List<Resenia>> getReseñaByProfesionista(@PathVariable Long idProfesionista){
        return ResponseEntity.ok(reseniaService.obtenerReseñasByProfesionista(idProfesionista));
    }
    @GetMapping("/stats/{idProfesionista}")
    public ResponseEntity<ReseniaStats> getReseñaStatsByProfesionista(@PathVariable Long idProfesionista){
        return ResponseEntity.ok(reseniaService.estadisticasReseniasByProfesionista(idProfesionista));
    }
    @GetMapping("/{idReseña}")
    public ResponseEntity<Resenia> getReseñaById(@PathVariable Long idReseña){
        return ResponseEntity.ok(reseniaService.obtenerReseñaById(idReseña));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postReseña(@RequestBody ReseniaDTOPost reseniaDTOPost){
        return ResponseEntity.ok(reseniaService.registrarReseña(reseniaDTOPost));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> deleteReseña(@PathVariable Long id){
        return ResponseEntity.ok(reseniaService.borrarReseñaById(id));
    }
}
