package tesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reseñas")
public class ReseniaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reseña")
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private int calificacion;

    @Column(name = "fecha_reseña")
    private LocalDate fechaResenia;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_profesionista", nullable = false)
    private ProfesionistaEntity profesionista;
}
