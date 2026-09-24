package sptech.classicamoveis.Movimentacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioContagemItemDto(

        @NotNull(message = "O produto é obrigatório")
        Integer produtoId,

        @NotNull(message = "A quantidade contada é obrigatória")
        @Min(value = 0, message = "A quantidade contada não pode ser negativa")
        Integer quantidadeContada

) {
}