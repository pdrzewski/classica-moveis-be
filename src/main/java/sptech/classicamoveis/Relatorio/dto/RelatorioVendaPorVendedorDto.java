package sptech.classicamoveis.Relatorio.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendaPorVendedorDto {

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Integer vendedorId;
    private String vendedorNome;
    private String cargo;
    private Double salario;
    private Integer comissao;
    private Double valorComissao;

    private List<RelatorioVendaVendedorVendaDto> vendas = new ArrayList<>();

    private Double totalVendido;

    public RelatorioVendaPorVendedorDto() {
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(Integer vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getVendedorNome() {
        return vendedorNome;
    }

    public void setVendedorNome(String vendedorNome) {
        this.vendedorNome = vendedorNome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Integer getComissao() {
        return comissao;
    }

    public void setComissao(Integer comissao) {
        this.comissao = comissao;
    }

    public Double getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(Double valorComissao) {
        this.valorComissao = valorComissao;
    }

    public List<RelatorioVendaVendedorVendaDto> getVendas() {
        return vendas;
    }

    public void setVendas(List<RelatorioVendaVendedorVendaDto> vendas) {
        this.vendas = vendas;
    }

    public Double getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(Double totalVendido) {
        this.totalVendido = totalVendido;
    }
}
