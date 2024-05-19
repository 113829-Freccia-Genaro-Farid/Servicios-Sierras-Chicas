package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.auxiliar.*;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.*;
import tesis.services.auxiliar.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/auxiliares")
public class AuxiliarController {
    @Autowired
    CiudadService ciudadService;
    @Autowired
    ProvinciaService provinciaService;
    @Autowired
    TipoDNIService tipoDNIService;
    @Autowired
    RolService rolService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProfesionService profesionService;

    @GetMapping("/provincias")
    public ResponseEntity<List<Provincia>> getProvincias(){
        return ResponseEntity.ok(provinciaService.obtenerProvincias());
    }
    @PostMapping("/provincias")
    public ResponseEntity<MensajeRespuesta> postProvincia(@RequestBody ProvinciaDTO provinciaDTO){
        return ResponseEntity.ok(provinciaService.registrar(provinciaDTO));
    }
    @DeleteMapping("/provincias/{id}")
    public ResponseEntity<Boolean> deleteProvincia(@PathVariable Long id){
        return ResponseEntity.ok(provinciaService.borrarById(id));
    }
    @GetMapping("/ciudades")
    public ResponseEntity<List<Ciudad>> getCiudadesPorProvincia(@RequestParam(name = "idProvincia") Long idProvincia){
        return ResponseEntity.ok(ciudadService.obtenerCiudadesPorProvincia(idProvincia));
    }
    @PostMapping("/ciudades")
    public ResponseEntity<MensajeRespuesta> postCiudad(@RequestBody CiudadDTO ciudadDTO){
        return ResponseEntity.ok(ciudadService.registrar(ciudadDTO));
    }
    @DeleteMapping("/ciudades/{id}")
    public ResponseEntity<Boolean> deleteCiudad(@PathVariable Long id){
        return ResponseEntity.ok(ciudadService.borrarById(id));
    }
    @GetMapping("/tiposDNI")
    public ResponseEntity<List<TipoDNI>> getTiposDNI(){
        return ResponseEntity.ok(tipoDNIService.obtenerTipos());
    }
    @PostMapping("/tiposDNI")
    public ResponseEntity<MensajeRespuesta> postTipoDNI(@RequestBody TipoDNIDto tipoDNIDto){
        return ResponseEntity.ok(tipoDNIService.registrar(tipoDNIDto));
    }
    @DeleteMapping("/tiposDNI/{id}")
    public ResponseEntity<Boolean> deleteTipoDNI(@PathVariable Long id){
        return ResponseEntity.ok(tipoDNIService.borrarById(id));
    }
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> getRoles(){
        return ResponseEntity.ok(rolService.obtenerRoles());
    }
    @PostMapping("/roles")
    public ResponseEntity<MensajeRespuesta> postRol(@RequestBody RolDTO rolDTO){
        return ResponseEntity.ok(rolService.registrar(rolDTO));
    }
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Boolean> deleteRol(@PathVariable Long id){
        return ResponseEntity.ok(rolService.borrarById(id));
    }
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias(){
        return ResponseEntity.ok(categoriaService.obtenerCategorias());
    }
    @PostMapping("/categorias")
    public ResponseEntity<MensajeRespuesta> postCategoria(@RequestBody CategoriaDTO categoriaDTO){
        return ResponseEntity.ok(categoriaService.registrar(categoriaDTO));
    }
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Boolean> deleteCategoria(@PathVariable Long id){
        return ResponseEntity.ok(categoriaService.borrarById(id));
    }
    @GetMapping("/profesiones")
    public ResponseEntity<List<Profesion>> getProfesionesPorCategoria(@RequestParam(name = "idCategoria") Long idCategoria){
        return ResponseEntity.ok(profesionService.obtenerProfesionesPorCategoria(idCategoria));
    }
    @PostMapping("/profesiones")
    public ResponseEntity<MensajeRespuesta> postProfesion(@RequestBody ProfesionDTO profesionDTO){
        return ResponseEntity.ok(profesionService.registrar(profesionDTO));
    }
    @DeleteMapping("/profesiones/{id}")
    public ResponseEntity<Boolean> deleteProfesion(@PathVariable Long id){
        return ResponseEntity.ok(profesionService.borrarById(id));
    }

}
