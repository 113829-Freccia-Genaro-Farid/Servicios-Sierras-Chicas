package tesis.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoDTOPut {
    String descripcion;
    LocalDateTime fechaTurno;
    Long idCliente;
    Long idProfesionista;
}
