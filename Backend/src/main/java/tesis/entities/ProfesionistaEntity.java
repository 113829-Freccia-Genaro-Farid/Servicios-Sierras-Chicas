package tesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tesis.entities.auxiliar.ProfesionEntity;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profesionistas")
public class ProfesionistaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesionista")
    private Long id;

    @Column(name = "posee_matricula",nullable = false)
    private boolean PoseeMatricula;

    @Column
    private String NroMatricula;

    @Column(name = "comunicacion_wsp",nullable = false)
    private boolean ComunicacionWsp;

    @Column
    private String presentacion;

    @Column
    private boolean Suscrito;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "profesionista_profesiones",
            joinColumns = @JoinColumn(name = "id_profesionista"),
            inverseJoinColumns = @JoinColumn(name = "id_profesion"))
    private List<ProfesionEntity> profesiones;

    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaEntity persona;
}
