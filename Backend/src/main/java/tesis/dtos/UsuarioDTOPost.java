package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTOPost {
    String email;
    String password;
    boolean activo;
    LocalDate fechaAlta;
    Long idRol;
}
