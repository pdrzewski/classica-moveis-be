package sptech.classicamoveis.Estoque;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Produto.Produto;

@Entity
@Table(name = "estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @EmbeddedId
    private EstoqueId id = new EstoqueId();

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @MapsId("estabelecimentoId")
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;

    @Column(nullable = false)
    private Integer qtd;
}
