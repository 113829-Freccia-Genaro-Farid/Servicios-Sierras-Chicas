package tesis.entities.auxiliar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recuperacionPassword")
public class RecuperacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "codigo_recuperacion")
    private String codigoRecuperacion;

    @Column
    private String email;

    @Column
    private LocalDateTime fechaSolicitud;
}
