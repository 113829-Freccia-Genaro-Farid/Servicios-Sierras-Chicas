package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionistaDTO {
    Long id;
    boolean PoseeMatricula;
    String NroMatricula;
    boolean ComunicacionWsp;
    String presentacion;
    List<String> profesiones;
    boolean Suscrito;
    PersonaDTO persona;
}

