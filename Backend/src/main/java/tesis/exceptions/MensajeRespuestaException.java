package tesis.exceptions;

import lombok.Getter;
import tesis.dtos.common.MensajeRespuesta;

@Getter
public class MensajeRespuestaException extends RuntimeException {
    private final MensajeRespuesta mensajeRespuesta;

    public MensajeRespuestaException(MensajeRespuesta mensajeRespuesta) {
        super(mensajeRespuesta.getMensaje());
        this.mensajeRespuesta = mensajeRespuesta;
    }

}


