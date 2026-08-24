package sptech.classicamoveis.Movimentacao.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoResponseDto;
import sptech.classicamoveis.Movimentacao.mapper.MovimentacaoMapper;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemRepository;
    private final MovimentacaoMapper mapper;
    private final ColaboradorRepository colaboradorRepository;
    private final ClienteRepository clienteRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public MovimentacaoResponseDto buscarPorId(Integer id) {
        Movimentacao mov = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada com id: " + id));
        
        MovimentacaoResponseDto dto = mapper.toResponseDTO(mov);
        List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(id);
        dto.setItens(mapper.toItemResponseDTOList(itens));
        
        return dto;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseDto> listarVendasPendentes(Integer estabelecimentoId) {
        List<Movimentacao> vendas = movimentacaoRepository.findByTipoMovimentacaoAndStatus(
                TipoMovimentacao.VENDA,
                StatusMovimentacao.PENDENTE
        );
        
        List<Movimentacao> vendasFiltradas = new java.util.ArrayList<>();
        if (estabelecimentoId != null) {
            for (Movimentacao venda : vendas) {
                if (venda.getEstabelecimentoOrigem() != null && 
                    venda.getEstabelecimentoOrigem().getId().equals(estabelecimentoId)) {
                    vendasFiltradas.add(venda);
                }
            }
        } else {
            vendasFiltradas.addAll(vendas);
        }
        
        List<MovimentacaoResponseDto> dtoList = new java.util.ArrayList<>();
        for (Movimentacao mov : vendasFiltradas) {
            MovimentacaoResponseDto dto = mapper.toResponseDTO(mov);
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(mov.getId());
            dto.setItens(mapper.toItemResponseDTOList(itens));
            dtoList.add(dto);
        }
        return dtoList;
    }

    public MovimentacaoResponseDto criar(MovimentacaoRequestDto requestDto) {
        validarMovimentacao(requestDto);

        Movimentacao mov = new Movimentacao();
        mov.setDataHora(LocalDateTime.now());
        mov.setTipoMovimentacao(requestDto.getTipoMovimentacao());
        mov.setFormaPagamento(requestDto.getFormaPagamento());
        mov.setObservacao(requestDto.getObservacao());

        // Definir status inicial conforme tipo
        mov.setStatus(definirStatusInicial(requestDto.getTipoMovimentacao()));

        // Carregar referências
        mov.setColaborador(colaboradorRepository.findById(requestDto.getColaboradorId())
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado")));

        if (requestDto.getEstabelecimentoOrigemId() != null) {
            mov.setEstabelecimentoOrigem(estabelecimentoRepository.findById(requestDto.getEstabelecimentoOrigemId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento origem não encontrado")));
        }

        if (requestDto.getEstabelecimentoDestinoId() != null) {
            mov.setEstabelecimentoDestino(estabelecimentoRepository.findById(requestDto.getEstabelecimentoDestinoId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento destino não encontrado")));
        }

        if (requestDto.getClienteId() != null) {
            mov.setCliente(clienteRepository.findById(requestDto.getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado")));
        }

        if (requestDto.getFornecedorId() != null) {
            mov.setFornecedor(fornecedorRepository.findById((long) requestDto.getFornecedorId())
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado")));
        }

        // Calcular valor total
        double valorTotal = requestDto.getItens().stream()
                .mapToDouble(ItemMovimentacaoRequestDto::getSubtotal)
                .sum();
        mov.setValorTotal(valorTotal);

        Movimentacao saved = movimentacaoRepository.save(mov);

        // Criar itens
        for (ItemMovimentacaoRequestDto itemDto : requestDto.getItens()) {
            ItemMovimentacao item = new ItemMovimentacao();
            item.setMovimentacao(saved);
            item.setProduto(produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado")));
            item.setQtd(itemDto.getQuantidade());
            item.setPrecoUnitario(itemDto.getValorUnitario());
            item.setDesconto(itemDto.getDesconto());
            itemRepository.save(item);
        }

        MovimentacaoResponseDto dto = mapper.toResponseDTO(saved);
        List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(saved.getId());
        dto.setItens(mapper.toItemResponseDTOList(itens));

        return dto;
    }

    public MovimentacaoResponseDto concluir(Integer id) {
        Movimentacao mov = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada"));

        if (!mov.getTipoMovimentacao().equals(TipoMovimentacao.VENDA)) {
            throw new IllegalArgumentException("Apenas vendas pendentes podem ser concluídas");
        }

        if (!mov.getStatus().equals(StatusMovimentacao.PENDENTE)) {
            throw new IllegalArgumentException("Apenas movimentações pendentes podem ser concluídas");
        }

        // Validar estoque disponível
        List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(id);
        for (ItemMovimentacao item : itens) {
            double saldoDisponivel = estoqueService.calcularSaldoDisponivel(
                    item.getProduto().getId(),
                    mov.getEstabelecimentoOrigem().getId()
            );
            if (saldoDisponivel < item.getQtd()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + item.getProduto().getNome());
            }
        }

        mov.setStatus(StatusMovimentacao.CONCLUIDO);
        Movimentacao saved = movimentacaoRepository.save(mov);

        MovimentacaoResponseDto dto = mapper.toResponseDTO(saved);
        dto.setItens(mapper.toItemResponseDTOList(itens));

        return dto;
    }

    public MovimentacaoResponseDto cancelar(Integer id) {
        Movimentacao mov = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada"));

        if (!mov.getStatus().equals(StatusMovimentacao.PENDENTE)) {
            throw new IllegalArgumentException("Apenas movimentações pendentes podem ser canceladas");
        }

        mov.setStatus(StatusMovimentacao.CANCELADO);
        Movimentacao saved = movimentacaoRepository.save(mov);

        MovimentacaoResponseDto dto = mapper.toResponseDTO(saved);
        List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(saved.getId());
        dto.setItens(mapper.toItemResponseDTOList(itens));

        return dto;
    }

    private StatusMovimentacao definirStatusInicial(TipoMovimentacao tipo) {
        return switch (tipo) {
            case VENDA -> StatusMovimentacao.PENDENTE;
            case COMPRA, TRANSFERENCIA, AJUSTE_ENTRADA, AJUSTE_SAIDA -> StatusMovimentacao.CONCLUIDO;
        };
    }

    private void validarMovimentacao(MovimentacaoRequestDto dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("A movimentação deve conter pelo menos um item");
        }

        switch (dto.getTipoMovimentacao()) {
            case VENDA -> validarVenda(dto);
            case COMPRA -> validarCompra(dto);
            case TRANSFERENCIA -> validarTransferencia(dto);
            case AJUSTE_ENTRADA, AJUSTE_SAIDA -> validarAjuste(dto);
        }
    }

    private void validarVenda(MovimentacaoRequestDto dto) {
        if (dto.getEstabelecimentoOrigemId() == null || dto.getClienteId() == null || 
            dto.getColaboradorId() == null || dto.getFormaPagamento() == null) {
            throw new IllegalArgumentException("Venda deve ter: estabelecimento origem, cliente, colaborador e forma de pagamento");
        }

        if (dto.getEstabelecimentoDestinoId() != null || dto.getFornecedorId() != null) {
            throw new IllegalArgumentException("Venda não pode ter estabelecimento destino ou fornecedor");
        }
    }

    private void validarCompra(MovimentacaoRequestDto dto) {
        if (dto.getEstabelecimentoDestinoId() == null || dto.getFornecedorId() == null || 
            dto.getColaboradorId() == null || dto.getFormaPagamento() == null) {
            throw new IllegalArgumentException("Compra deve ter: estabelecimento destino, fornecedor, colaborador e forma de pagamento");
        }

        if (dto.getEstabelecimentoOrigemId() != null || dto.getClienteId() != null) {
            throw new IllegalArgumentException("Compra não pode ter estabelecimento origem ou cliente");
        }
    }

    private void validarTransferencia(MovimentacaoRequestDto dto) {
        if (dto.getEstabelecimentoOrigemId() == null || dto.getEstabelecimentoDestinoId() == null || 
            dto.getColaboradorId() == null) {
            throw new IllegalArgumentException("Transferência deve ter: estabelecimento origem, destino e colaborador");
        }

        if (dto.getEstabelecimentoOrigemId().equals(dto.getEstabelecimentoDestinoId())) {
            throw new IllegalArgumentException("Origem e destino devem ser estabelecimentos diferentes");
        }

        if (dto.getClienteId() != null || dto.getFornecedorId() != null || dto.getFormaPagamento() != null) {
            throw new IllegalArgumentException("Transferência não pode ter cliente, fornecedor ou forma de pagamento");
        }
    }

    private void validarAjuste(MovimentacaoRequestDto dto) {
        if (dto.getEstabelecimentoOrigemId() == null || dto.getColaboradorId() == null || 
            dto.getObservacao() == null || dto.getObservacao().isEmpty()) {
            throw new IllegalArgumentException("Ajuste deve ter: estabelecimento origem, colaborador e observação");
        }

        if (dto.getEstabelecimentoDestinoId() != null || dto.getClienteId() != null || 
            dto.getFornecedorId() != null || dto.getFormaPagamento() != null) {
            throw new IllegalArgumentException("Ajuste não pode ter estabelecimento destino, cliente, fornecedor ou forma de pagamento");
        }
    }

    public MovimentacaoResponseDto criarMovimentacao(MovimentacaoRequestDto requestDto) {
        return criar(requestDto);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseDto> buscarVendasPendentes(Integer estabelecimentoId, 
            java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        return listarVendasPendentes(estabelecimentoId);
    }
}
