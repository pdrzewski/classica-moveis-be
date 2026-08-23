package sptech.classicamoveis.Colaborador;

import java.time.LocalDate;

public record AniversarioColaboradorDto(
        Integer id,
        String nome,
        LocalDate dataNascimento,
        int diasParaAniversario
) {}