package sptech.classicamoveis.CargoHasPermissao;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Cargo.Cargo;
import sptech.classicamoveis.Permissao.Permissao;

@Entity
@Table(name = "cargo_has_permissao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoHasPermissao {

    @EmbeddedId
    private CargoHasPermissaoId id = new CargoHasPermissaoId();

    @ManyToOne
    @MapsId("cargoId")
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne
    @MapsId("permissaoId")
    @JoinColumn(name = "permissao_id")
    private Permissao permissao;
}
