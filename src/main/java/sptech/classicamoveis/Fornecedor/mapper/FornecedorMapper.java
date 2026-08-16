package sptech.classicamoveis.Fornecedor.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.mapper.EnderecoMapper;
import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorResponseDTO;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;

@Component
@RequiredArgsConstructor
public class FornecedorMapper {

    private final EnderecoMapper enderecoMapper;

    public Fornecedor toEntity(FornecedorRequestDTO dto, Endereco endereco) {
        return Fornecedor.builder()
                .nome(dto.nome())
                .cnpj(dto.cnpj())
                .representante(dto.representante())
                .telefone1(dto.telefone1())
                .telefone2(dto.telefone2())
                .endereco(endereco)
                .build();
    }

    public void updateEntityFromDto(FornecedorRequestDTO dto, Endereco endereco, Fornecedor fornecedor) {
        fornecedor.setNome(dto.nome());
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setRepresentante(dto.representante());
        fornecedor.setTelefone1(dto.telefone1());
        fornecedor.setTelefone2(dto.telefone2());
        fornecedor.setEndereco(endereco);
    }

    public FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getRepresentante(),
                fornecedor.getTelefone1(),
                fornecedor.getTelefone2(),
                enderecoMapper.toResponseDTO(fornecedor.getEndereco())
        );
    }
}