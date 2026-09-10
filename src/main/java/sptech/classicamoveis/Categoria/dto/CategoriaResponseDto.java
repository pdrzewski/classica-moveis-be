package sptech.classicamoveis.Categoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de categoria retornados pela API")
public class CategoriaResponseDto {

    @Schema(example = "3")
    private Integer id;
    @Schema(example = "Sofás e Poltronas")
    private String categoria;

    public String getNome() {
        return categoria;
    }

    public void setNome(String nome) {
        this.categoria = nome;
    }
}
