package sptech.classicamoveis.Compra;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Movimentacao.Movimentacao;

@Entity
@Table(name = "compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "fk_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @Column(name = "valor_total")
    private Double valorTotal;

    @OneToOne
    @JoinColumn(name = "fk_movimentacao", nullable = false, unique = true)
    private Movimentacao movimentacao;
}
