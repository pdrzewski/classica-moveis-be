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
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.dto.PagamentoRequestDto;
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
 private MovimentacaoRequestDto base(TipoMovimentacao t){return new MovimentacaoRequestDto(t,List.of(new PagamentoRequestDto(FormaPagamento.PIX,100.0,1)),"obs",1.0,1,2,3,4,5,List.of(item()));}
 @Test void rejeitaSemItens(){var d=base(TipoMovimentacao.VENDA);d.setItens(List.of());assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigeOrigemClienteColaboradorPagamento(){var d=base(TipoMovimentacao.VENDA);d.setEstabelecimentoOrigemId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaNaoAceitaDestino(){var d=base(TipoMovimentacao.VENDA);d.setEstabelecimentoDestinoId(2);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void compraExigeDestinoFornecedor(){var d=base(TipoMovimentacao.COMPRA);d.setEstabelecimentoDestinoId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void compraNaoAceitaOrigem(){var d=base(TipoMovimentacao.COMPRA);d.setEstabelecimentoOrigemId(1);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaExigeOrigemDestinoColaborador(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setEstabelecimentoOrigemId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaNaoPodeMesmaLoja(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setClienteId(null);d.setFornecedorId(null);d.setPagamentos(null);d.setEstabelecimentoOrigemId(1);d.setEstabelecimentoDestinoId(1);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaNaoAceitaCliente(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setPagamentos(null);d.setClienteId(3);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void ajusteExigeObservacao(){var d=base(TipoMovimentacao.AJUSTE_ENTRADA);d.setEstabelecimentoDestinoId(null);d.setClienteId(null);d.setFornecedorId(null);d.setPagamentos(null);d.setObservacao("");assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void ajusteNaoAceitaCliente(){var d=base(TipoMovimentacao.AJUSTE_SAIDA);d.setEstabelecimentoDestinoId(null);d.setClienteId(3);d.setFornecedorId(null);d.setPagamentos(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void buscarPorId(){Movimentacao m=new Movimentacao();m.setId(1);var dto=new MovimentacaoResponseDto();when(movRepo.findById(1)).thenReturn(Optional.of(m));when(mapper.toResponseDTO(m)).thenReturn(dto);when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertSame(dto,service.buscarPorId(1));}
 @Test void buscarPorIdInexistente(){when(movRepo.findById(1)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarPorId(1));}
 @Test void listarPendentesSemFiltro(){Movimentacao m=new Movimentacao();m.setId(1);when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of(m));var dto=new MovimentacaoResponseDto();when(mapper.toResponseDTO(m)).thenReturn(dto);when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertEquals(1,service.listarVendasPendentes(null).size());}
 @Test void listarPendentesFiltraLoja(){Movimentacao a=new Movimentacao();a.setId(1);sptech.classicamoveis.Estabelecimento.Estabelecimento e=new sptech.classicamoveis.Estabelecimento.Estabelecimento();e.setId(1);a.setEstabelecimentoOrigem(e);Movimentacao b=new Movimentacao();b.setId(2);when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of(a,b));when(mapper.toResponseDTO(a)).thenReturn(new MovimentacaoResponseDto());when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());assertEquals(1,service.listarVendasPendentes(1).size());}
 @Test void cancelar(){Movimentacao m=new Movimentacao();m.setId(1);m.setStatus(StatusMovimentacao.PENDENTE);when(movRepo.findById(1)).thenReturn(Optional.of(m));when(movRepo.save(m)).thenReturn(m);when(mapper.toResponseDTO(m)).thenReturn(new MovimentacaoResponseDto());when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());service.cancelar(1);assertEquals(StatusMovimentacao.CANCELADO,m.getStatus());}
 @Test void cancelarNaoPendente(){Movimentacao m=new Movimentacao();m.setStatus(StatusMovimentacao.CONCLUIDO);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.cancelar(1));}
 @Test void concluirExigeVenda(){Movimentacao m=new Movimentacao();m.setTipoMovimentacao(TipoMovimentacao.COMPRA);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.concluir(1));}
 @Test void concluirExigePendente(){Movimentacao m=new Movimentacao();m.setTipoMovimentacao(TipoMovimentacao.VENDA);m.setStatus(StatusMovimentacao.CONCLUIDO);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.concluir(1));}

 // Cobertura adicional das validações de entrada e dos caminhos de erro.
 @Test void rejeitaRequestNulo(){assertThrows(IllegalArgumentException.class,()->service.criar(null));}
 @Test void rejeitaTipoNulo(){var d=base(TipoMovimentacao.VENDA);d.setTipoMovimentacao(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaListaItensNula(){var d=base(TipoMovimentacao.VENDA);d.setItens(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaProdutoNuloNoItem(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setProdutoId(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaQuantidadeNula(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setQuantidade(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaQuantidadeZero(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setQuantidade(0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaValorUnitarioNulo(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setValorUnitario(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaValorUnitarioNegativo(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setValorUnitario(-1.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaDescontoNegativo(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setDesconto(-1.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void rejeitaDescontoMaiorQueSubtotal(){var d=base(TipoMovimentacao.VENDA);d.getItens().get(0).setDesconto(201.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigePagamento(){var d=base(TipoMovimentacao.VENDA);d.setPagamentos(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaListaPagamentoVazia(){var d=base(TipoMovimentacao.VENDA);d.setPagamentos(List.of());assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigeFrete(){var d=base(TipoMovimentacao.VENDA);d.setFrete(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaFreteNegativo(){var d=base(TipoMovimentacao.VENDA);d.setFrete(-1.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigeFormaPagamento(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setFormaPagamento(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaExigeValorPagamento(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setValor(null);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaValorPagamentoZero(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setValor(0.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaParcelaZero(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setQuantidadeParcelas(0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaParcelamentoPix(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setQuantidadeParcelas(2);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void vendaRejeitaTotalDePagamentoDiferente(){var d=base(TipoMovimentacao.VENDA);d.getPagamentos().get(0).setValor(10.0);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void compraRejeitaCliente(){var d=base(TipoMovimentacao.COMPRA);d.setEstabelecimentoOrigemId(null);d.setClienteId(2);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void transferenciaRejeitaFornecedor(){var d=base(TipoMovimentacao.TRANSFERENCIA);d.setPagamentos(null);d.setClienteId(null);d.setFornecedorId(4);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void ajusteRejeitaDestino(){var d=base(TipoMovimentacao.AJUSTE_ENTRADA);d.setClienteId(null);d.setFornecedorId(null);d.setPagamentos(null);d.setEstabelecimentoDestinoId(5);assertThrows(IllegalArgumentException.class,()->service.criar(d));}
 @Test void concluirInexistente(){when(movRepo.findById(404)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.concluir(404));}
 @Test void cancelarInexistente(){when(movRepo.findById(404)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.cancelar(404));}
 @Test void ocultarInexistente(){when(movRepo.findById(404)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.ocultar(404));}
 @Test void ocultarNaoPermitePendente(){Movimentacao m=new Movimentacao();m.setStatus(StatusMovimentacao.PENDENTE);when(movRepo.findById(1)).thenReturn(Optional.of(m));assertThrows(IllegalArgumentException.class,()->service.ocultar(1));}
 @Test void ocultarConcluida(){Movimentacao m=new Movimentacao();m.setId(1);m.setStatus(StatusMovimentacao.CONCLUIDO);when(movRepo.findById(1)).thenReturn(Optional.of(m));when(movRepo.save(m)).thenReturn(m);when(mapper.toResponseDTO(m)).thenReturn(new MovimentacaoResponseDto());when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of());when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());service.ocultar(1);assertFalse(m.getVisivel());}


 // Caminhos de sucesso adicionais para exercitar regras de negócio e montagem dos DTOs.
 @Test void concluirVendaPendenteComSucesso(){
  Movimentacao m=new Movimentacao(); m.setId(7); m.setTipoMovimentacao(TipoMovimentacao.VENDA); m.setStatus(StatusMovimentacao.PENDENTE);
  when(movRepo.findById(7)).thenReturn(Optional.of(m)); when(itemRepo.findByMovimentacaoId(7)).thenReturn(List.of());
  when(movRepo.save(m)).thenReturn(m); MovimentacaoResponseDto dto=new MovimentacaoResponseDto(); when(mapper.toResponseDTO(m)).thenReturn(dto); when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());
  assertSame(dto, service.concluir(7)); assertEquals(StatusMovimentacao.CONCLUIDO,m.getStatus()); assertNotNull(m.getDataHoraEntrega()); verify(movRepo).save(m);
 }
 @Test void listarPendentesRetornaListaVazia(){
  when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of());
  assertTrue(service.listarVendasPendentes(null).isEmpty());
 }
 @Test void listarPendentesIgnoraVendaDeOutraLojaEOrigemNula(){
  Movimentacao semOrigem=new Movimentacao(); semOrigem.setId(10);
  Movimentacao outraLoja=new Movimentacao(); outraLoja.setId(11); sptech.classicamoveis.Estabelecimento.Estabelecimento loja=new sptech.classicamoveis.Estabelecimento.Estabelecimento(); loja.setId(2); outraLoja.setEstabelecimentoOrigem(loja);
  when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA,StatusMovimentacao.PENDENTE)).thenReturn(List.of(semOrigem,outraLoja));
  assertTrue(service.listarVendasPendentes(1).isEmpty()); verifyNoInteractions(mapper);
 }
 @Test void ocultarMovimentacaoCancelada(){
  Movimentacao m=new Movimentacao(); m.setId(12); m.setStatus(StatusMovimentacao.CANCELADO); when(movRepo.findById(12)).thenReturn(Optional.of(m)); when(movRepo.save(m)).thenReturn(m);
  MovimentacaoResponseDto dto=new MovimentacaoResponseDto(); when(mapper.toResponseDTO(m)).thenReturn(dto); when(itemRepo.findByMovimentacaoId(12)).thenReturn(List.of()); when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());
  assertSame(dto,service.ocultar(12)); assertFalse(m.getVisivel());
 }
 @Test void historicoVazioRetornaPaginaVazia(){
  when(movRepo.buscarHistorico(isNull(),isNull(),any(org.springframework.data.domain.Pageable.class))).thenReturn(org.springframework.data.domain.Page.empty());
  var resultado=service.buscarHistorico(null,null,0,10); assertTrue(resultado.isEmpty());
 }

}
