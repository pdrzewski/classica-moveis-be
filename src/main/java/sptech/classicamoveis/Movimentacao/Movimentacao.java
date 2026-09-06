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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public StatusMovimentacao getStatus() {
        return status;
    }

    public void setStatus(StatusMovimentacao status) {
        this.status = status;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Estabelecimento getEstabelecimentoOrigem() {
        return estabelecimentoOrigem;
    }

    public void setEstabelecimentoOrigem(Estabelecimento estabelecimentoOrigem) {
        this.estabelecimentoOrigem = estabelecimentoOrigem;
    }

    public Estabelecimento getEstabelecimentoDestino() {
        return estabelecimentoDestino;
    }

    public void setEstabelecimentoDestino(Estabelecimento estabelecimentoDestino) {
        this.estabelecimentoDestino = estabelecimentoDestino;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
