package tesis.dtos.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBuyer {
    private String title;
    private int quantity;
    private int price;
}
