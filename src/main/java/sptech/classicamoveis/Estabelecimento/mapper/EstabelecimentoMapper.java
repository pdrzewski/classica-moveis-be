package sptech.classicamoveis.Estabelecimento.mapper;

import org.springframework.stereotype.Component;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;

@Component
public class EstabelecimentoMapper {

    public EstabelecimentoResponseDto toResponseDTO(Estabelecimento e) {
        if (e == null) return null;
        Integer enderecoId = e.getEndereco() == null ? null : e.getEndereco().getId();
        Integer responsavelId = e.getResponsavel() == null ? null : e.getResponsavel().getId();
        
        return new EstabelecimentoResponseDto(
                e.getId(),
                e.getNome(),
                e.getCnpj(),
                e.getTelefone(),
                responsavelId,
                enderecoId,
                e.getEndereco().getCep(),
                e.getEndereco().getLogradouro(),
                e.getEndereco().getBairro(),
                e.getEndereco().getCidade(),
                e.getEndereco().getNumero(),
                e.getEndereco().getComplemento(),
                e.getEndereco().getEstado()
        );
    }
}
