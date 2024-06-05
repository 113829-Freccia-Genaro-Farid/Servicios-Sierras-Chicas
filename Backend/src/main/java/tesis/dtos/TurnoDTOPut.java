package tesis.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoDTOPut {
    LocalDate fechaTurno;
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm")
    LocalTime horaTurno;
    Long idCliente;
    Long idProfesionista;
}
