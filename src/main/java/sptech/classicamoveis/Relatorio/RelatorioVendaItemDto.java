package sptech.classicamoveis.Relatorio;

import java.time.LocalDateTime;

public record RelatorioVendaItemDto(
        Integer idVenda,
        LocalDateTime dataHora,
        String nomeLoja,
        String nomeProduto,
        Integer quantidade,
        Double precoUnitario,
        Double subtotal
) {}