package tesis.entities.auxiliar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ciudades")
public class CiudadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;

    @Column
    private String descripcion;

    @Column
    private String codigoPostal;

    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private ProvinciaEntity provincia;
}
