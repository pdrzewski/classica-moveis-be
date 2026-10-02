package sptech.classicamoveis.Relatorio.dto;

import java.util.ArrayList;
import java.util.List;

public class RelatorioTabelaPrecosDto {

    private String estabelecimento;
    private String fornecedor;
    private List<RelatorioTabelaPrecosItemDto> produtos = new ArrayList<>();

    public RelatorioTabelaPrecosDto() {
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<RelatorioTabelaPrecosItemDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<RelatorioTabelaPrecosItemDto> produtos) {
        this.produtos = produtos;
    }
}
