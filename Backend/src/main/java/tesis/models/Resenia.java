package tesis.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.dtos.ClienteDTO;
import tesis.dtos.ProfesionistaDTO;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resenia {
    Long id;
    String descripcion;
    int calificacion;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate fechaResenia;
    ClienteDTO cliente;
    ProfesionistaDTO profesionista;
}
