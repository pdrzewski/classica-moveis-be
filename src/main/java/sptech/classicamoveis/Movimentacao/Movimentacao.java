package sptech.classicamoveis.Movimentacao;

import jakarta.persistence.*;
import lombok.*;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.FormaPagamento.FormaPagamento;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusMovimentacao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @Column(length = 100)
    private String observacao;

    @Column(name = "valor_total")
    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_origem_id")
    private Estabelecimento estabelecimentoOrigem;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_destino_id")
    private Estabelecimento estabelecimentoDestino;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}
