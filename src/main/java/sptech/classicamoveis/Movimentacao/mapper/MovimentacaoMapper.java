package sptech.classicamoveis.Movimentacao.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoResponseDto;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoResponseDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovimentacaoMapper {

    public MovimentacaoResponseDto toResponseDTO(Movimentacao movimentacao) {
        if (movimentacao == null) return null;

        MovimentacaoResponseDto dto = new MovimentacaoResponseDto();
        dto.setId(movimentacao.getId());
        dto.setDataHora(movimentacao.getDataHora());
        dto.setTipoMovimentacao(movimentacao.getTipoMovimentacao());
        dto.setStatus(movimentacao.getStatus());
        dto.setFormaPagamento(movimentacao.getFormaPagamento());
        dto.setObservacao(movimentacao.getObservacao());
        dto.setValorTotal(movimentacao.getValorTotal());

        if (movimentacao.getColaborador() != null) {
            dto.setColaboradorId(Math.toIntExact(movimentacao.getColaborador().getId()));
            dto.setColaboradorNome(movimentacao.getColaborador().getNome());
        }

        if (movimentacao.getEstabelecimentoOrigem() != null) {
            dto.setEstabelecimentoOrigemId(movimentacao.getEstabelecimentoOrigem().getId());
            dto.setEstabelecimentoOrigemNome(movimentacao.getEstabelecimentoOrigem().getNome());
        }

        if (movimentacao.getEstabelecimentoDestino() != null) {
            dto.setEstabelecimentoDestinoId(movimentacao.getEstabelecimentoDestino().getId());
            dto.setEstabelecimentoDestinoNome(movimentacao.getEstabelecimentoDestino().getNome());
        }

        if (movimentacao.getCliente() != null) {
            dto.setClienteId(movimentacao.getCliente().getId());
            dto.setClienteNome(movimentacao.getCliente().getNome());
        }

        if (movimentacao.getFornecedor() != null) {
            dto.setFornecedorId(Math.toIntExact(movimentacao.getFornecedor().getId()));
            dto.setFornecedorNome(movimentacao.getFornecedor().getNome());
        }

        return dto;
    }

    public ItemMovimentacaoResponseDto toItemResponseDTO(ItemMovimentacao item) {
        if (item == null) return null;

        ItemMovimentacaoResponseDto dto = new ItemMovimentacaoResponseDto();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoNome(item.getProduto().getNome());
        dto.setQuantidade(item.getQtd());
        dto.setValorUnitario(item.getPrecoUnitario());
        dto.setDesconto(item.getDesconto());
        dto.setSubtotal(item.getSubtotal());

        return dto;
    }

    public List<ItemMovimentacaoResponseDto> toItemResponseDTOList(List<ItemMovimentacao> itens) {
        List<ItemMovimentacaoResponseDto> dtoList = new java.util.ArrayList<>();
        for (ItemMovimentacao item : itens) {
            dtoList.add(toItemResponseDTO(item));
        }
        return dtoList;
    }
}
