package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionistaDTOPut {
    boolean poseeMatricula;
    String nroMatricula;
    boolean comunicacionWsp;
    List<Long> idProfesiones;
}
