package tesis.dtos.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CiudadDTO {
    String descripcion;
    String codigoPostal;
    Long id_provincia;
}
