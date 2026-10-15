package sptech.classicamoveis.Mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.mapper.ColaboradorMapper;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Endereco.mapper.EnderecoMapper;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorMapperTest {

    @Mock EnderecoMapper enderecoMapper;
    @InjectMocks ColaboradorMapper mapper;

    @Test
    void nuloRetornaNulo() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void mapeiaColaboradorCompleto() {
        Cargo cargo = new Cargo();
        cargo.setId(2);
        cargo.setCargo("GERENTE");
        Estabelecimento loja = new Estabelecimento();
        loja.setId(3);
        Colaborador c = new Colaborador();
        c.setId(1);
        c.setNome("Ana");
        c.setCargo(cargo);
        c.setUsuario(new Usuario(4, "ana", "hash"));
        c.setEstabelecimento(loja);
        c.setEmFerias(false);
        c.setDataAdmissao(LocalDate.of(2020, 1, 10));
        c.setDataNascimento(LocalDate.of(1990, 5, 20));
        c.setSalario(3500.0);
        c.setComissao(5);
        c.setCpf("12345678900");
        c.setTelefone1("11999999999");

        ColaboradorResponseDto dto = mapper.toResponseDTO(c);

        assertEquals(1, dto.getId());
        assertEquals("Ana", dto.getNome());
        assertEquals(2, dto.getCargoId());
        assertEquals("GERENTE", dto.getCargoNome());
        assertEquals(4, dto.getUsuarioId());
        assertEquals(3, dto.getEstabelecimentoId());
        assertEquals(false, dto.getEmFerias());
        assertEquals(LocalDate.of(2020, 1, 10), dto.getDataAdmissao());
        assertEquals(3500.0, dto.getSalario());
        assertEquals(5, dto.getComissao());
        assertEquals("12345678900", dto.getCpf());
        assertEquals("11999999999", dto.getTelefone1());
    }

    @Test
    void relacionamentosAusentesViramNulos() {
        Colaborador c = new Colaborador();
        c.setId(1);

        ColaboradorResponseDto dto = mapper.toResponseDTO(c);

        assertNull(dto.getCargoId());
        assertNull(dto.getCargoNome());
        assertNull(dto.getUsuarioId());
        assertNull(dto.getEstabelecimentoId());
        assertNull(dto.getEndereco());
    }
}
