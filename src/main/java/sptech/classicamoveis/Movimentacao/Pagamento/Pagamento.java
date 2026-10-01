package sptech.classicamoveis.Movimentacao.Pagamento;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.Movimentacao;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Integer quantidadeParcelas;

    @ManyToOne
    @JoinColumn(name = "movimentacao_id", nullable = false)
    private Movimentacao movimentacao;
}