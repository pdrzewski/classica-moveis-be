package sptech.classicamoveis.Relatorio.dto;

public class RelatorioVendaVendedorItemDto {

    private Integer produtoId;
    private String sku;
    private String produtoNome;
    private Integer quantidade;
    private Double valorUnitario;
    private Double totalItem;

    public RelatorioVendaVendedorItemDto() {
    }

    public RelatorioVendaVendedorItemDto(
            Integer produtoId,
            String sku,
            String produtoNome,
            Integer quantidade,
            Double valorUnitario,
            Double totalItem
    ) {
        this.produtoId = produtoId;
        this.sku = sku;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.totalItem = totalItem;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Double totalItem) {
        this.totalItem = totalItem;
    }
}