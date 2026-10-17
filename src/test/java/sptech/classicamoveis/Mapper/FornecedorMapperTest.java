package sptech.classicamoveis.Mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;
import sptech.classicamoveis.Endereco.mapper.EnderecoMapper;
import sptech.classicamoveis.Fornecedor.dto.FornecedorComEnderecoRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorResponseDTO;
import sptech.classicamoveis.Fornecedor.mapper.FornecedorMapper;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FornecedorMapperTest {

    @Mock EnderecoMapper enderecoMapper;
    @InjectMocks FornecedorMapper mapper;

    private final Endereco endereco = new Endereco(1, "01001000", "Rua A", "Centro", "SP", "10", null, "SP");

    private FornecedorRequestDTO request() {
        return new FornecedorRequestDTO("Forn", "12345678000199", "1111", "2222", "3333", "a@b.com",
                1, null, null, null, null, null, null, null);
    }

    @Test
    void toEntityCopiaCamposEEndereco() {
        Fornecedor f = mapper.toEntity(request(), endereco);

        assertEquals("Forn", f.getNome());
        assertEquals("12345678000199", f.getCnpj());
        assertEquals("1111", f.getTelefone1());
        assertEquals("2222", f.getTelefone2());
        assertEquals("3333", f.getWhatsapp());
        assertEquals("a@b.com", f.getEmail());
        assertSame(endereco, f.getEndereco());
    }

    @Test
    void toEntityFromComEnderecoCopiaCamposEEndereco() {
        FornecedorComEnderecoRequestDTO dto = new FornecedorComEnderecoRequestDTO(
                "Forn", "12345678000199", "1111", "2222", "3333", "a@b.com",
                "01001000", "Rua A", "Centro", "SP", "10", null, "SP");

        Fornecedor f = mapper.toEntityFromComEnderecoDTO(dto, endereco);

        assertEquals("Forn", f.getNome());
        assertEquals("a@b.com", f.getEmail());
        assertSame(endereco, f.getEndereco());
    }

    @Test
    void updateEntityFromDtoSobrescreveOsCampos() {
        Fornecedor existente = new Fornecedor();
        existente.setNome("antigo");

        mapper.updateEntityFromDto(request(), endereco, existente);

        assertEquals("Forn", existente.getNome());
        assertEquals("12345678000199", existente.getCnpj());
        assertEquals("3333", existente.getWhatsapp());
        assertSame(endereco, existente.getEndereco());
    }

    @Test
    void toResponseDtoDelegaOEnderecoParaOEnderecoMapper() {
        Fornecedor f = mapper.toEntity(request(), endereco);
        f.setId(5L);
        EnderecoResponseDTO enderecoDto = new EnderecoResponseDTO(1, "01001000", "Rua A", "Centro", "SP", "10", null, "SP");
        when(enderecoMapper.toResponseDTO(endereco)).thenReturn(enderecoDto);

        FornecedorResponseDTO dto = mapper.toResponseDTO(f);

        assertEquals(5L, dto.id());
        assertEquals("Forn", dto.nome());
        assertSame(enderecoDto, dto.endereco());
    }
}
