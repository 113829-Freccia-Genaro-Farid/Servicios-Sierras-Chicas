package tesis.entities.auxiliar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profesiones")
public class ProfesionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesion")
    private Long id;

    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;
}
