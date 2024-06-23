package tesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "anuncios")
public class AnuncioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anuncio")
    private Long id;

    @Column
    private BigDecimal cantidadClicks;

    @Column
    private int anio;

    @Column
    private int mes;

    @ManyToOne
    @JoinColumn(name = "id_profesionista", nullable = false)
    private ProfesionistaEntity profesionista;
}