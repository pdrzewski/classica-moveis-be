package sptech.classicamoveis.Colaborador;

import java.time.LocalDate;

public record FeriasRequestDto(
        LocalDate dataInicio,
        LocalDate dataFim
) {}