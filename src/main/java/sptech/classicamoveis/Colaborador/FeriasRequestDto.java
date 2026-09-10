package sptech.classicamoveis.Colaborador;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Período de férias do colaborador")
public record FeriasRequestDto(
        @Schema(example = "2026-12-01") LocalDate dataInicio,
        @Schema(example = "2026-12-30") LocalDate dataFim
) {}
