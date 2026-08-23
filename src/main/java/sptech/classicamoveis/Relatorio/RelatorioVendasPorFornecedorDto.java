package sptech.classicamoveis.Relatorio;

public record RelatorioVendasPorFornecedorDto(
        Integer fornecedorId,
        String nomeFornecedor,
        Long quantidadeVendida,
        Double valorTotalVendido
) {}