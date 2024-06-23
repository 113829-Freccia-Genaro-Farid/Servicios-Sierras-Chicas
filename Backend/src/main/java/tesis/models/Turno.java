package tesis.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ProfesionistaDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turno {
    Long idTurno;
    String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime fechaTurno;
    ClienteDTO cliente;
    ProfesionistaDTO profesionista;
}
