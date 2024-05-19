package tesis.models.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesion {
    Long id;
    String descripcion;
    Long idCategoria;
}
