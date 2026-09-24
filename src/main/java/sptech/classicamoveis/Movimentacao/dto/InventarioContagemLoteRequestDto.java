package sptech.classicamoveis.Movimentacao.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventarioContagemLoteRequestDto(

        @NotNull(message = "O colaborador é obrigatório")
        Integer colaboradorId,

        @NotEmpty(message = "O inventário deve possuir pelo menos um produto")
        @Valid
        List<InventarioContagemItemDto> itens

) {
}