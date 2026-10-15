package sptech.classicamoveis.Mapper;

import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;
import sptech.classicamoveis.Estabelecimento.mapper.EstabelecimentoMapper;

import static org.junit.jupiter.api.Assertions.*;

class EstabelecimentoMapperTest {

    private final EstabelecimentoMapper mapper = new EstabelecimentoMapper();

    private Estabelecimento estabelecimento(Colaborador responsavel) {
        Estabelecimento e = new Estabelecimento();
        e.setId(1);
        e.setNome("Loja Centro");
        e.setCnpj("12345678000199");
        e.setTelefone("1133334444");
        e.setEndereco(new Endereco(7, "01001000", "Rua A", "Centro", "São Paulo", "10", "Sala 1", "SP"));
        e.setResponsavel(responsavel);
        return e;
    }

    @Test
    void nuloRetornaNulo() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void mapeiaDadosEEnderecoComResponsavel() {
        Colaborador resp = new Colaborador();
        resp.setId(9);

        EstabelecimentoResponseDto dto = mapper.toResponseDTO(estabelecimento(resp));

        assertEquals(1, dto.getId());
        assertEquals("Loja Centro", dto.getNome());
        assertEquals("12345678000199", dto.getCnpj());
        assertEquals("1133334444", dto.getTelefone());
        assertEquals(9, dto.getResponsavelId());
        assertEquals(7, dto.getEnderecoId());
        assertEquals("01001000", dto.getCep());
        assertEquals("Rua A", dto.getLogradouro());
        assertEquals("Centro", dto.getBairro());
        assertEquals("São Paulo", dto.getCidade());
        assertEquals("10", dto.getNumero());
        assertEquals("Sala 1", dto.getComplemento());
        assertEquals("SP", dto.getEstado());
    }

    @Test
    void responsavelAusenteResultaEmIdNulo() {
        assertNull(mapper.toResponseDTO(estabelecimento(null)).getResponsavelId());
    }
}
