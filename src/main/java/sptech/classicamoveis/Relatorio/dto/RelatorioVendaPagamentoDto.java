package sptech.classicamoveis.Relatorio.dto;

public class RelatorioVendaPagamentoDto {

    private String formaPagamento;
    private Double valor;
    private Integer quantidadeParcelas;

    public RelatorioVendaPagamentoDto() {
    }

    public RelatorioVendaPagamentoDto(
            String formaPagamento,
            Double valor,
            Integer quantidadeParcelas
    ) {
        this.formaPagamento = formaPagamento;
        this.valor = valor;
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }
}