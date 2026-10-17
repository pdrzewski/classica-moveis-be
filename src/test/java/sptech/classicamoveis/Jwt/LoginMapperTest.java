package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.mapper.ColaboradorMapper;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Jwt.dto.LoginResponseDto;
import sptech.classicamoveis.Jwt.mapper.LoginMapper;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginMapperTest {

    @Mock ColaboradorMapper colaboradorMapper;
    @InjectMocks LoginMapper mapper;

    @Test
    void usuarioNuloRetornaNulo() {
        assertNull(mapper.toResponseDTO(null, List.of()));
    }

    @Test
    void mapeiaLoginPermissoesUsuarioEColaborador() {
        Colaborador colaborador = new Colaborador();
        ColaboradorResponseDto colaboradorDto = new ColaboradorResponseDto();
        when(colaboradorMapper.toResponseDTO(colaborador)).thenReturn(colaboradorDto);
        UsuarioAutenticado u = new UsuarioAutenticado(new Usuario(7, "ana", "hash"), colaborador, false);

        LoginResponseDto dto = mapper.toResponseDTO(u, List.of("VENDER"));

        assertEquals("ana", dto.getLogin());
        assertEquals(List.of("VENDER"), dto.getPermissoes());
        assertEquals(7, dto.getUsuarioId());
        assertSame(colaboradorDto, dto.getColaborador());
    }
}
