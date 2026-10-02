package sptech.classicamoveis.Relatorio.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendaVendedorVendaDto {

    private Integer vendaId;
    private LocalDateTime dataHoraVenda;
    private Double valorTotalVenda;
    private List<RelatorioVendaVendedorItemDto> itens = new ArrayList<>();

    public RelatorioVendaVendedorVendaDto() {
    }

    public RelatorioVendaVendedorVendaDto(
            Integer vendaId,
            LocalDateTime dataHoraVenda,
            Double valorTotalVenda,
            List<RelatorioVendaVendedorItemDto> itens
    ) {
        this.vendaId = vendaId;
        this.dataHoraVenda = dataHoraVenda;
        this.valorTotalVenda = valorTotalVenda;
        this.itens = itens;
    }

    public Integer getVendaId() {
        return vendaId;
    }

    public void setVendaId(Integer vendaId) {
        this.vendaId = vendaId;
    }

    public LocalDateTime getDataHoraVenda() {
        return dataHoraVenda;
    }

    public void setDataHoraVenda(LocalDateTime dataHoraVenda) {
        this.dataHoraVenda = dataHoraVenda;
    }

    public Double getValorTotalVenda() {
        return valorTotalVenda;
    }

    public void setValorTotalVenda(Double valorTotalVenda) {
        this.valorTotalVenda = valorTotalVenda;
    }

    public List<RelatorioVendaVendedorItemDto> getItens() {
        return itens;
    }

    public void setItens(List<RelatorioVendaVendedorItemDto> itens) {
        this.itens = itens;
    }
}
