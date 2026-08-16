package sptech.classicamoveis.Produto.model;

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

    @Column(nullable = false, length = 45)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(length = 45)
    private String sku;

    @Column(name = "codigo_barras", length = 45)
    private String codigoBarras;

    @Column(name = "unidade_medida", length = 45)
    private String unidadeMedida;

    @Column(length = 45)
    private String marca;

    @Column(name = "preco_custo", nullable = false)
    private Double precoCusto;

    @Column(name = "preco_venda", nullable = false)
    private Double precoVenda;

    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo;

    @Column(nullable = false)
    private Boolean ativo;
}
