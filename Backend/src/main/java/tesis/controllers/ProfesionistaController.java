package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.AnuncioDTO;
import tesis.dtos.ProfesionistaDTO;
import tesis.dtos.ProfesionistaDTOPost;
import tesis.dtos.ProfesionistaDTOPut;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.ProfesionistaService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/profesionistas")
public class ProfesionistaController {
    @Autowired
    ProfesionistaService profesionistaService;
    @GetMapping("")
    public ResponseEntity<List<ProfesionistaDTO>> getProfesionistas(){
        return ResponseEntity.ok(profesionistaService.listarProfesionistas());
    }
    @GetMapping("/ordenada")
    public ResponseEntity<List<ProfesionistaDTO>> getProfesionistasOrdenada(){
        return ResponseEntity.ok(profesionistaService.listarProfesionistasOrdenadaSuscritos());
    }
    @GetMapping("/filtrada")
    public ResponseEntity<List<ProfesionistaDTO>> getProfesionistasFiltrada(@RequestParam(name = "nombreApllido", required = false) String nombreApellido,
                                                                            @RequestParam(name = "categorias", required = false) List<Long> categorias,
                                                                            @RequestParam(name = "profesiones", required = false) List<Long> profesiones,
                                                                            @RequestParam(name = "ciudades", required = false) List<Long> ciudades){
        return ResponseEntity.ok(profesionistaService.listaProfesionistasConFiltros(nombreApellido,categorias,profesiones,ciudades));
    }
    @GetMapping("/{idProfesionista}")
    public ResponseEntity<ProfesionistaDTO> getProfesionistaById(@PathVariable Long idProfesionista){
        return ResponseEntity.ok(profesionistaService.obtenerProfesionistaById(idProfesionista));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postProfesionista(@RequestBody ProfesionistaDTOPost profesionistaDTOPost){
        return ResponseEntity.ok(profesionistaService.registrarProfesionista(profesionistaDTOPost));
    }
    @PutMapping("/{idProfesionista}")
    public ResponseEntity<MensajeRespuesta> putProfesionista(@PathVariable Long idProfesionista, @RequestBody ProfesionistaDTOPut profesionistaDTOPut){
        return ResponseEntity.ok(profesionistaService.modificarProfesionista(idProfesionista, profesionistaDTOPut));
    }
    @PutMapping("/presentacion/{idProfesionista}")
    public ResponseEntity<MensajeRespuesta> putPresentacionProfesionista(@PathVariable Long idProfesionista, @RequestBody String nuevaPresentacion){
        return ResponseEntity.ok(profesionistaService.modificarPresentacionProfesionista(idProfesionista, nuevaPresentacion));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> deleteProfesionista(@PathVariable Long id){
        return ResponseEntity.ok(profesionistaService.borrarProfesionistaById(id));
    }

    // ***************************************************
    // ****************** ANUNCIOS ***********************
    // ***************************************************

    @GetMapping("/anuncios")
    public ResponseEntity<List<AnuncioDTO>> getAnuncios(){
        return ResponseEntity.ok(profesionistaService.getAnuncios());
    }
    @GetMapping("/anuncios/{idProfesionista}")
    public ResponseEntity<List<AnuncioDTO>> getAnuncios(@PathVariable Long idProfesionista){
        return ResponseEntity.ok(profesionistaService.getAnunciosByProfesionista(idProfesionista));
    }
    @PostMapping("/clickAnuncio/{profesionistaId}")
    public ResponseEntity<MensajeRespuesta> postClickAnuncio(@PathVariable Long profesionistaId){
        return ResponseEntity.ok(profesionistaService.clickEnAnuncio(profesionistaId));
    }
}

