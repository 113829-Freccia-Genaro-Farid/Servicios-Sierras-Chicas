package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionistaDTO {
    Long id;
    boolean PoseeMatricula;
    String NroMatricula;
    boolean ComunicacionWsp;
    List<String> profesiones;
    PersonaDTO persona;
}
