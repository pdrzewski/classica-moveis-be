package sptech.classicamoveis.Mapper;

import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;
import sptech.classicamoveis.Endereco.mapper.EnderecoMapper;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoMapperTest {

    private final EnderecoMapper mapper = new EnderecoMapper();

    @Test
    void nuloRetornaNulo() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void mapeiaTodosOsCampos() {
        Endereco e = new Endereco(1, "01001000", "Rua A", "Centro", "São Paulo", "10", "Apto 2", "SP");

        EnderecoResponseDTO dto = mapper.toResponseDTO(e);

        assertEquals(1, dto.id());
        assertEquals("01001000", dto.cep());
        assertEquals("Rua A", dto.logradouro());
        assertEquals("Centro", dto.bairro());
        assertEquals("São Paulo", dto.cidade());
        assertEquals("10", dto.numero());
        assertEquals("Apto 2", dto.complemento());
        assertEquals("SP", dto.estado());
    }
}
