package tesis.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnuncioDTO {
    Long id;
    BigDecimal cantidadClicks;
    int anio;
    int mes;
    ProfesionistaDTO profesionista;
}
