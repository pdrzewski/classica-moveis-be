package sptech.classicamoveis.Movimentacao.ItemMovimentacao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Produto.model.Produto;

@Entity
@Table(name = "item_movimentacao", uniqueConstraints = {
        @UniqueConstraint(name = "uk_item_movimentacao", columnNames = {"fk_movimentacao", "fk_produto"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(value = 1)
    private Integer qtd;

    @ManyToOne
    @JoinColumn(name = "fk_movimentacao", nullable = false)
    private Movimentacao movimentacao;

    @ManyToOne
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;

    @Column(name = "preco_unitario", nullable = false)
    private Double precoUnitario;

    @Column(name = "desconto")
    private Double desconto;

    public Double getSubtotal() {
        return (qtd * precoUnitario) - (desconto != null ? desconto : 0);
    }
}