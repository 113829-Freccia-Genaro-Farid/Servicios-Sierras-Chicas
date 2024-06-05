package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaDTOPost {
    String descripcion;
    int calificacion;
    Long clienteId;
    Long profesionistaId;
}
