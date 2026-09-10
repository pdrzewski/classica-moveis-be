package sptech.classicamoveis.Categoria.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação/atualização de categoria de produto")
public class CategoriaRequestDto {

    @JsonAlias({"nome"})
    @Schema(example = "Sofás e Poltronas")
    private String categoria;

    public String getNome() {
        return categoria;
    }

    public void setNome(String nome) {
        this.categoria = nome;
    }
}
