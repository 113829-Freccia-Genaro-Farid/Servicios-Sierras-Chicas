package tesis.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tesis.dtos.common.MensajeRespuesta;
import tesis.exceptions.MensajeRespuestaException;

@ControllerAdvice
public class ExceptionsHandlerController {
    @ExceptionHandler(MensajeRespuestaException.class)
    public ResponseEntity<MensajeRespuesta> handleMensajeRespuestaException(MensajeRespuestaException ex) {
        MensajeRespuesta mensajeRespuesta = ex.getMensajeRespuesta();
        return new ResponseEntity<>(mensajeRespuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
