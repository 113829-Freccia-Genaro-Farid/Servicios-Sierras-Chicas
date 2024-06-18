package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ClienteDTOPost;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.ClienteService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    ClienteService clienteService;
    @GetMapping("")
    public ResponseEntity<List<ClienteDTO>> getClientes(){
        return ResponseEntity.ok(clienteService.listarClientes());
    }
    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long idCliente){
        return ResponseEntity.ok(clienteService.obtenerClienteById(idCliente));
    }
    @GetMapping("/byUserEmail/{email}")
    public ResponseEntity<ClienteDTO> getClienteByUserEmail(@PathVariable String email){
        return ResponseEntity.ok(clienteService.obtenerClienteByUserEmail(email));
    }
    @PostMapping("")
    public ResponseEntity<MensajeRespuesta> postCliente(@RequestBody ClienteDTOPost clienteDTOPost){
        return ResponseEntity.ok(clienteService.registrarCliente(clienteDTOPost));
    }
//    @PutMapping("/{idCliente}")
//    public ResponseEntity<MensajeRespuesta> putCliente(@PathVariable Long idCliente, @RequestBody ClienteDTOPut clienteDTOPut){
//        return ResponseEntity.ok(clienteService.modificarCliente(idCliente, clienteDTOPut));
//    }
}
