package sptech.classicamoveis.Colaborador;

import java.time.LocalDate;

public record ColaboradorRequestDto(
        String nome,

        Integer cargoId,

        Integer usuarioId,

        Boolean emFerias,

        LocalDate dataAdmissao,

        LocalDate dataNascimento,

        Double salario,

        String carteiraTrabalho,

        Integer comissao
) {}
