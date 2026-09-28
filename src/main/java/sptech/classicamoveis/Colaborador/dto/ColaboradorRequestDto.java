package sptech.classicamoveis.Colaborador.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Dados para criação/atualização de colaborador")
public record ColaboradorRequestDto(
        @Schema(example = "João Pedro Lima") String nome,

        @Schema(example = "2") Integer cargoId,

        @Schema(example = "5") Integer usuarioId,

        @Schema(example = "false") Boolean emFerias,

        @Schema(example = "2023-03-01") LocalDate dataAdmissao,

        @Schema(example = "1995-07-22") LocalDate dataNascimento,

        @Schema(example = "2200.00") Double salario,

        @Schema(example = "1234567") String carteiraTrabalho,

        @Schema(description = "Percentual de comissão sobre vendas", example = "3") Integer comissao,

        @Schema(example = "1") Integer estabelecimentoId,

        @Schema(example = "38912233045") String cpf,

        @Size(max = 20, message = "Telefone1 deve ter no máximo 20 caracteres") @Schema(example = "11912345678") String telefone1,

        @Size(max = 20, message = "Telefone2 deve ter no máximo 20 caracteres") @Schema(example = "11987654321") String telefone2,

        @Schema(example = "22") Integer enderecoId
) {
}
