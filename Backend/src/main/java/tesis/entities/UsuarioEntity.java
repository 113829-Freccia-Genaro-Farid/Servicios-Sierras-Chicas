package tesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.entities.auxiliar.RolEntity;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String password;

    @Column
    private boolean activo;

    @Column
    private LocalDate fechaAlta;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RolEntity rol;
}
