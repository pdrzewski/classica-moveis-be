package sptech.classicamoveis.Usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais para criação de usuário")
public record UsuarioRequestDto(
        @Schema(example = "joao.lima") String login,
        @Schema(example = "SenhaForte@123") String senha
) {}
