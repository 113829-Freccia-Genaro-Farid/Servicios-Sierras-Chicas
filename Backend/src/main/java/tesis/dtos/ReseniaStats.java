package tesis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaStats {
    int unaEstrella;
    int dosEstrella;
    int tresEstrella;
    int cuatroEstrella;
    int cincoEstrella;
    int totalResenias;
}
