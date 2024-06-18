package tesis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tesis.dtos.auxiliar.EmailValuesDTO;
import tesis.dtos.common.MensajeRespuesta;
import tesis.services.impl.EmailService;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/email/send")
    public ResponseEntity<?> sendEmail() {
        emailService.sendEmail();
        return new ResponseEntity<>("Correo enviado con éxito", HttpStatus.OK);
    }

    @PostMapping("/email/send-html")
    public ResponseEntity<MensajeRespuesta> sendEmailTemplate(@RequestBody EmailValuesDTO dto) {
        return ResponseEntity.ok(emailService.sendEmailTemplate(dto));
    }
}
