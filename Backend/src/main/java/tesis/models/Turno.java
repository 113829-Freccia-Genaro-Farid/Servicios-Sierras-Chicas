package tesis.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ProfesionistaDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turno {
    Long idTurno;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate fechaTurno;
    @JsonFormat(pattern = "HH:mm")
    LocalTime horaTurno;
    ClienteDTO cliente;
    ProfesionistaDTO profesionista;
}
