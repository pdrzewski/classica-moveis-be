package sptech.classicamoveis.Movimentacao.mapper;

import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.Pagamento.Pagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Produto.model.Produto;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovimentacaoMapperTest {
    private final MovimentacaoMapper mapper = new MovimentacaoMapper();

    @Test void movimentacaoNulaRetornaNulo() { assertNull(mapper.toResponseDTO(null)); }
    @Test void itemNuloRetornaNulo() { assertNull(mapper.toItemResponseDTO(null)); }
    @Test void mapeiaMovimentacaoSemRelacionamentos() {
        Movimentacao m = new Movimentacao(); m.setId(5); m.setTipoMovimentacao(TipoMovimentacao.VENDA);
        m.setStatus(StatusMovimentacao.PENDENTE); m.setObservacao("teste"); m.setValorTotal(120.0); m.setFrete(10.0);
        var dto = mapper.toResponseDTO(m);
        assertEquals(5, dto.getId()); assertEquals("teste", dto.getObservacao());
        assertEquals(120.0, dto.getValorTotal()); assertEquals(10.0, dto.getFrete());
    }
    @Test void mapeiaPagamentos() {
        Movimentacao m = new Movimentacao();
        Pagamento p = new Pagamento(); p.setId(2); p.setFormaPagamento(FormaPagamento.PIX); p.setValor(55.0); p.setQuantidadeParcelas(1);
        m.setPagamentos(List.of(p));
        var dto = mapper.toResponseDTO(m);
        assertEquals(1, dto.getPagamentos().size()); assertEquals(55.0, dto.getPagamentos().get(0).getValor());
        assertEquals(FormaPagamento.PIX, dto.getPagamentos().get(0).getFormaPagamento());
    }
    @Test void mapeiaItemComDesconto() {
        Produto produto = new Produto(); produto.setId(9); produto.setNome("Sofá");
        ItemMovimentacao item = new ItemMovimentacao(); item.setId(4); item.setProduto(produto); item.setQtd(2);
        item.setPrecoUnitario(100.0); item.setDesconto(10.0);
        var dto = mapper.toItemResponseDTO(item);
        assertEquals(9, dto.getProdutoId()); assertEquals("Sofá", dto.getProdutoNome());
        assertEquals(2, dto.getQuantidade()); assertEquals(190.0, dto.getSubtotal());
    }
    @Test void listaDeItensVazia() { assertTrue(mapper.toItemResponseDTOList(List.of()).isEmpty()); }
}
