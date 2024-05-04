package tesis.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    String email;
    boolean activo;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate fechaAlta;
    String rol;
}
