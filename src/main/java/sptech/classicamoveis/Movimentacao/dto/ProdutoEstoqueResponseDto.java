package sptech.classicamoveis.Movimentacao.dto;

public class ProdutoEstoqueResponseDto {

    private Integer produtoId;
    private String nome;
    private String sku;
    private Long saldoDisponivel;
    private Integer estoqueMinimo;
    private String fornecedor;

    public ProdutoEstoqueResponseDto() {
    }

    public ProdutoEstoqueResponseDto(Integer produtoId, String nome, String sku, Long saldoDisponivel, Integer estoqueMinimo, String fornecedor) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.sku = sku;
        this.saldoDisponivel = saldoDisponivel;
        this.estoqueMinimo = estoqueMinimo;
        this.fornecedor = fornecedor;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public void setSaldoDisponivel(Long saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getFornecedor() {return fornecedor;}

    public void setFornecedor(String fornecedor) {this.fornecedor = fornecedor;}
}
