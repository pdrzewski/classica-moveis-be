package sptech.classicamoveis.Transferencia;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Movimentacao.Movimentacao;

@Entity
@Table(name = "transferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_estabelecimento_origem", nullable = false)
    private Estabelecimento estabelecimentoOrigem;

    @ManyToOne
    @JoinColumn(name = "fk_estabelecimento_destino", nullable = false)
    private Estabelecimento estabelecimentoDestino;

    @OneToOne
    @JoinColumn(name = "fk_movimentacao", nullable = false, unique = true)
    private Movimentacao movimentacao;
}
