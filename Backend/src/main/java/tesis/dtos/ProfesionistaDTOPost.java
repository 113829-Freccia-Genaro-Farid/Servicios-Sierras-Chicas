package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionistaDTOPost {
    boolean PoseeMatricula;
    String NroMatricula;
    boolean ComunicacionWsp;
    String presentacion;
    List<Long> idProfesiones;
    Long idPersona;
}

