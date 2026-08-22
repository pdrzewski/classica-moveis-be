package sptech.classicamoveis.Categoria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDto {

    private Integer id;
    private String categoria;

    public String getNome() {
        return categoria;
    }

    public void setNome(String nome) {
        this.categoria = nome;
    }
}
