package sptech.classicamoveis.CargoHasPermissao;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoHasPermissaoId implements Serializable {
    private Integer cargoId;
    private Integer permissaoId;
}