package sptech.classicamoveis.Relatorio.dto;

public class RelatorioVendaProdutoDto {

    private String codigoProduto;
    private Integer quantidade;
    private String nomeProduto;
    private Double precoUnitario;
    private Double desconto;
    private Double total;

    public RelatorioVendaProdutoDto() {
    }

    public RelatorioVendaProdutoDto(
            String codigoProduto,
            Integer quantidade,
            String nomeProduto,
            Double precoUnitario,
            Double desconto,
            Double total
    ) {
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.desconto = desconto;
        this.total = total;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}