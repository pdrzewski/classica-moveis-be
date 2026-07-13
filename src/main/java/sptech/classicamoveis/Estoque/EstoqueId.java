package sptech.classicamoveis.Estoque;


import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueId implements Serializable {
    private Integer produtoId;
    private Integer estabelecimentoId;
}