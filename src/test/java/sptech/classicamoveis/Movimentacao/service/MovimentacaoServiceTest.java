package sptech.classicamoveis.Movimentacao.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.*;
import sptech.classicamoveis.Movimentacao.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.*;
import sptech.classicamoveis.Movimentacao.mapper.MovimentacaoMapper;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoServiceTest {
 @Mock MovimentacaoRepository movRepo; @Mock ItemMovimentacaoRepository itemRepo; @Mock MovimentacaoMapper mapper; @Mock ColaboradorRepository colaboradorRepo; @Mock ClienteRepository clienteRepo; @Mock EstabelecimentoRepository estabelecimentoRepo; @Mock FornecedorRepository fornecedorRepo; @Mock ProdutoRepository produtoRepo; @Mock EstoqueService estoque; @InjectMocks MovimentacaoService service;
 private ItemMovimentacaoRequestDto item(){return new ItemMovimentacaoRequestDto(1,2,100.0,10.0);}
 private MovimentacaoRequestDto base(TipoMovimentacao t){return new MovimentacaoRequestDto(t,FormaPagamento.PIX,"obs",1,2,3,4,5,List.of(item()));}
 @Test void rejeitaSemItens(){var d=base(TipoMovimentacao.VENDA);d.setItens(List.of());assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigeOrigemClienteColaboradorPagamento(){var d=base(TipoMovimentacao.VENDA);d.setEstabelecimentoOrigemId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaNaoAceitaDestino(){var d=base(TipoMovimentacao.VENDA);d.setEstabelecimentoDestinoId(2);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void compraExigeDestinoFornecedor(){var d=base(TipoMovimentacao.COMPRA);d.setEstabelecimentoDestinoId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void compraNaoAceitaOrigem(){var d=base(TipoMovimentacao.COMPRA);d.setEstabelecimentoOrigemId(1);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaExigeOrigemDestinoColaborador(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setEstabelecimentoOrigemId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaNaoPodeMesmaLoja(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setClienteId(null);d.setFornecedorId(null);d.setFormaPagamento(null);d.setEstabelecimentoOrigemId(1);d.setEstabelecimentoDestinoId(1);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaNaoAceitaCliente(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setFormaPagamento(null);d.setClienteId(3);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void ajusteExigeObservacao(){var d=base(TipoMovimentacao.AJUSTE_ENTRADA);d.setEstabelecimentoDestinoId(null);d.setClienteId(null);d.setFornecedorId(null);d.setFormaPagamento(null);d.setObservacao("");assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void ajusteNaoAceitaCliente(){var d=base(TipoMovimentacao.AJUSTE_SAIDA);d.setEstabelecimentoDestinoId(null);d.setClienteId(3);d.setFornecedorId(null);d.setFormaPagamento(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void buscarPorId(){Movimentacao m=new Movimentacao();m.setId(1);var dto=new MovimentacaoResponseDto();when(movRepo.findById(1)).thenReturn(Optional.of(m));when(mapper.toResponseDTO(m)).thenReturn(dto);when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertSame(dto,service.buscarPorId(1));}
 @Test void buscarPorIdInexistente(){when(movRepo.findById(1)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarPorId(1));}
 @Test void listarPendentesSemFiltro(){Movimentacao m=new Movimentacao();m.setId(1);when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of(m));var dto=new MovimentacaoResponseDto();when(mapper.toResponseDTO(m)).thenReturn(dto);when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertEquals(1,service.listarVendasPendentes(null).size());}
 @Test void listarPendentesFiltraLoja(){Movimentacao a=new Movimentacao();a.setId(1);sptech.classicamoveis.Estabelecimento.Estabelecimento e=new sptech.classicamoveis.Estabelecimento.Estabelecimento();e.setId(1);a.setEstabelecimentoOrigem(e);Movimentacao b=new Movimentacao();b.setId(2);when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of(a,b));when(mapper.toResponseDTO(a)).thenReturn(new MovimentacaoResponseDto());when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertEquals(1,service.listarVendasPendentes(1).size());}
 @Test void cancelar(){Movimentacao m=new Movimentacao();m.setId(1);m.setStatus(StatusMovimentacao.PENDENTE);when(movRepo.findById(1)).thenReturn(Optional.of(m));when(movRepo.save(m)).thenReturn(m);when(mapper.toResponseDTO(m)).thenReturn(new MovimentacaoResponseDto());when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());service.cancelar(1);assertEquals(StatusMovimentacao.CANCELADO,m.getStatus());}
 @Test void cancelarNaoPendente(){Movimentacao m=new Movimentacao();m.setStatus(StatusMovimentacao.CONCLUIDO);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.cancelar(1));}
 @Test void concluirExigeVenda(){Movimentacao m=new Movimentacao();m.setTipoMovimentacao(TipoMovimentacao.COMPRA);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.concluir(1));}
 @Test void concluirExigePendente(){Movimentacao m=new Movimentacao();m.setTipoMovimentacao(TipoMovimentacao.VENDA);m.setStatus(StatusMovimentacao.CONCLUIDO);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.concluir(1));}
}
