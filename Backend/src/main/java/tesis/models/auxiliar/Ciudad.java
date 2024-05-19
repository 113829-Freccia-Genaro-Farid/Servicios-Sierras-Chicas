package tesis.models.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ciudad {
    Long id;
    String descripcion;
    String codigoPostal;
    Long idProvincia;
}
