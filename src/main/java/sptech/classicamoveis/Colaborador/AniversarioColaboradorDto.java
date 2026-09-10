package sptech.classicamoveis.Colaborador;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Colaborador com aniversário próximo")
public record AniversarioColaboradorDto(
        @Schema(example = "5") Integer id,
        @Schema(example = "João Pedro Lima") String nome,
        @Schema(example = "1995-07-22") LocalDate dataNascimento,
        @Schema(example = "12") int diasParaAniversario
) {}
