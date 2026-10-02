package sptech.classicamoveis.Relatorio.dto;

public class RelatorioTabelaPrecosItemDto {

    private String codigoProduto;
    private String descricao;
    private Long quantidadeEstoque;
    private Double precoCompra;
    private Double precoVenda;

    public RelatorioTabelaPrecosItemDto() {
    }

    public RelatorioTabelaPrecosItemDto(
            String codigoProduto,
            String descricao,
            Long quantidadeEstoque,
            Double precoCompra,
            Double precoVenda
    ) {
        this.codigoProduto = codigoProduto;
        this.descricao = descricao;
        this.quantidadeEstoque = quantidadeEstoque;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Long quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(Double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = precoVenda;
    }
}