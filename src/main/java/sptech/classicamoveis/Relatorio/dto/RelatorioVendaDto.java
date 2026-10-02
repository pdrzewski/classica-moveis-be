package sptech.classicamoveis.Relatorio.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendaDto {

    private Integer numeroPedido;

    private LocalDateTime dataHoraGeracao;

    private LocalDateTime dataHoraEntrega;

    private String nomeCliente;

    private String documentoCliente;

    private String enderecoCompletoCliente;

    private String enderecoEntrega;

    private String telefoneCliente;

    private String nomeVendedor;

    private List<RelatorioVendaProdutoDto> produtos = new ArrayList<>();

    private List<RelatorioVendaPagamentoDto> pagamentos = new ArrayList<>();

    private Double totalProdutos;

    private Double desconto;

    private Double frete;

    private Double totalPedido;


    public RelatorioVendaDto() {
    }


    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }


    public LocalDateTime getDataHoraGeracao() {
        return dataHoraGeracao;
    }

    public void setDataHoraGeracao(LocalDateTime dataHoraGeracao) {
        this.dataHoraGeracao = dataHoraGeracao;
    }


    public LocalDateTime getDataHoraEntrega() {
        return dataHoraEntrega;
    }

    public void setDataHoraEntrega(LocalDateTime dataHoraEntrega) {
        this.dataHoraEntrega = dataHoraEntrega;
    }


    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }


    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }


    public String getEnderecoCompletoCliente() {
        return enderecoCompletoCliente;
    }

    public void setEnderecoCompletoCliente(String enderecoCompletoCliente) {
        this.enderecoCompletoCliente = enderecoCompletoCliente;
    }


    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }


    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }


    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }


    public List<RelatorioVendaProdutoDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<RelatorioVendaProdutoDto> produtos) {
        this.produtos = produtos;
    }


    public List<RelatorioVendaPagamentoDto> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<RelatorioVendaPagamentoDto> pagamentos) {
        this.pagamentos = pagamentos;
    }


    public Double getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(Double totalProdutos) {
        this.totalProdutos = totalProdutos;
    }


    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }


    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }


    public Double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(Double totalPedido) {
        this.totalPedido = totalPedido;
    }
}