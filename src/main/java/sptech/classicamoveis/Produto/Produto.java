package sptech.classicamoveis.Produto;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;

@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "fk_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "preco_custo")
    private Double precoCusto;

    @Column(name = "preco_venda")
    private Double precoVenda;

    @Column(name = "estoque_min")
    private Integer estoqueMin;

    @Column(length = 45)
    private String ncm;
}
