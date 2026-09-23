package sptech.classicamoveis.Jwt.mapper;

import org.springframework.stereotype.Component;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.mapper.ColaboradorMapper;
import sptech.classicamoveis.Jwt.dto.LoginResponseDto;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;

import java.util.List;

@Component
public class LoginMapper {

    private final ColaboradorMapper colaboradorMapper;

    public LoginMapper(ColaboradorMapper colaboradorMapper) {
        this.colaboradorMapper = colaboradorMapper;
    }

    public LoginResponseDto toResponseDTO(UsuarioAutenticado usuarioAutenticado, List<String> permissoes) {
        if (usuarioAutenticado == null) {
            return null;
        }

        ColaboradorResponseDto colaboradorDto = colaboradorMapper.toResponseDTO(usuarioAutenticado.getColaborador());
        return new LoginResponseDto(usuarioAutenticado.getUsername(), permissoes, usuarioAutenticado.getUsuario().getId(), colaboradorDto);
    }
}
