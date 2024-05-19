package tesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.entities.auxiliar.CiudadEntity;
import tesis.entities.auxiliar.TipoDNIEntity;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personas")
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Column(length = 25, nullable = false)
    private String apellido;

    @Column(length = 25, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private CiudadEntity ciudad;

    @Column(name = "fechaNac", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_tipodni")
    private TipoDNIEntity tipoDNI;

    @Column(name = "nro_doc", length = 10, nullable = false)
    private String nroDocumento;

    @Column(length = 50, nullable = false)
    private String calle;

    @Column(length = 10, nullable = false)
    private String altura;

    @Column(name = "nro_cel1", length = 12, nullable = false)
    private String telefono1;

    @Column(name = "nro_cel2", length = 12)
    private String telefono2;

    @Column(name = "tel_fijo", length = 12)
    private String telefonofijo;

    @Column
    private LocalDate fechaModificacion;

    @Column
    private boolean habilitado;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

}
