package sptech.classicamoveis.Venda;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Movimentacao.Movimentacao;

@Entity
@Table(name = "venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvenda")
    private Integer idVenda;

    @ManyToOne
    @JoinColumn(name = "fk_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", nullable = false)
    private Cliente cliente;
    private Double desconto;

    @Column(name = "valor_total")
    private Double valorTotal;

    @Column(name = "forma_pagamento", length = 45)
    private String formaPagamento;

    @OneToOne
    @JoinColumn(name = "fk_movimentacao", nullable = false, unique = true)
    private Movimentacao movimentacao;
}
