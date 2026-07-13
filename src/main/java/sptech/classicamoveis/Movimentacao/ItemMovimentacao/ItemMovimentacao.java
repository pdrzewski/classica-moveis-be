package sptech.classicamoveis.Movimentacao.ItemMovimentacao;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Produto.Produto;

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

    private Integer qtd;

    @ManyToOne
    @JoinColumn(name = "fk_movimentacao", nullable = false)
    private Movimentacao movimentacao;

    @ManyToOne
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;
}