package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.auxiliar.CiudadDTO;
import tesis.dtos.auxiliar.ProvinciaDTO;
import tesis.dtos.auxiliar.RolDTO;
import tesis.dtos.auxiliar.TipoDNIDto;
import tesis.dtos.common.MensajeRespuesta;
import tesis.models.auxiliar.Ciudad;
import tesis.models.auxiliar.Provincia;
import tesis.models.auxiliar.Rol;
import tesis.models.auxiliar.TipoDNI;
import tesis.services.auxiliar.CiudadService;
import tesis.services.auxiliar.ProvinciaService;
import tesis.services.auxiliar.RolService;
import tesis.services.auxiliar.TipoDNIService;

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

    @GetMapping("/provincias")
    public ResponseEntity<List<Provincia>> getProvincias(){
        return ResponseEntity.ok(provinciaService.obtenerProvincias());
    }
    @PostMapping("/provincias")
    public ResponseEntity<MensajeRespuesta> postProvincia(@RequestBody ProvinciaDTO provinciaDTO){
        return ResponseEntity.ok(provinciaService.registrar(provinciaDTO));
    }
    @DeleteMapping("/provincias")
    public ResponseEntity<Boolean> deleteProvincia(@RequestParam Long id){
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
    @DeleteMapping("/ciudades")
    public ResponseEntity<Boolean> deleteCiudad(@RequestParam Long id){
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
    @DeleteMapping("/tiposDNI")
    public ResponseEntity<Boolean> deleteTipoDNI(@RequestParam Long id){
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
    @DeleteMapping("/roles")
    public ResponseEntity<Boolean> deleteRol(@RequestParam Long id){
        return ResponseEntity.ok(rolService.borrarById(id));
    }
}
