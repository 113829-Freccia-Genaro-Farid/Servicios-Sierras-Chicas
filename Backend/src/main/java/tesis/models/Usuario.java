package tesis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    String email;
    String password;
    boolean activo;
    LocalDate fechaAlta;
    Long idRol;
}
