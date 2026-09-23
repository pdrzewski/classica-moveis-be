package sptech.classicamoveis.Colaborador.mapper;

import org.springframework.stereotype.Component;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.model.Colaborador;

@Component
public class ColaboradorMapper {

    public ColaboradorResponseDto toResponseDTO(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }

        Integer cargoId = colaborador.getCargo() == null ? null : colaborador.getCargo().getId();
        String cargoNome = colaborador.getCargo() == null ? null : colaborador.getCargo().getCargo();
        Integer usuarioId = colaborador.getUsuario() == null ? null : colaborador.getUsuario().getId();
        Integer estabelecimentoId = colaborador.getEstabelecimento() == null ? null : colaborador.getEstabelecimento().getId();

        return new ColaboradorResponseDto(
                colaborador.getId(),
                colaborador.getNome(),
                cargoId,
                cargoNome,
                usuarioId,
                colaborador.getEmFerias(),
                colaborador.getFeriasDataInicio(),
                colaborador.getFeriasDataFim(),
                colaborador.getDataAdmissao(),
                colaborador.getDataNascimento(),
                colaborador.getSalario(),
                colaborador.getCarteiraTrabalho(),
                colaborador.getComissao(),
                estabelecimentoId,
                colaborador.getCpf()
        );
    }
}
