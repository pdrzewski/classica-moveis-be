package sptech.classicamoveis.Endereco.mapper;

import org.springframework.stereotype.Component;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;

@Component
public class EnderecoMapper {

    public EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getEstado()
        );
    }
}