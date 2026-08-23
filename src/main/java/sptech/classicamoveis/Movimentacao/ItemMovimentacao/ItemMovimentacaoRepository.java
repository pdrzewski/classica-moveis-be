package sptech.classicamoveis.Movimentacao.ItemMovimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Relatorio.RelatorioVendaItemDto;
import sptech.classicamoveis.Relatorio.RelatorioVendasPorProdutoDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemMovimentacaoRepository extends JpaRepository<ItemMovimentacao, Integer> {

    List<ItemMovimentacao> findByMovimentacaoId(Integer movimentacaoId);

    @Query("SELECT new sptech.classicamoveis.Relatorio.RelatorioVendaItemDto(" +
            "v.idVenda, m.dataHora, e.nome, p.nome, im.qtd, im.precoUnitario, (im.qtd * im.precoUnitario)) " +
            "FROM ItemMovimentacao im " +
            "JOIN im.movimentacao m " +
            "JOIN im.produto p " +
            "JOIN Venda v ON v.movimentacao = m " +
            "JOIN v.estabelecimento e " +
            "WHERE p.fornecedor.id = :fornecedorId " +
            "AND (:idLoja IS NULL OR e.id = :idLoja) " +
            "ORDER BY m.dataHora DESC")
    List<RelatorioVendaItemDto> relatorioVendasPorFornecedor(
            @Param("fornecedorId") Integer fornecedorId,
            @Param("idLoja") Integer idLoja);

    @Query("SELECT new sptech.classicamoveis.Relatorio.RelatorioVendasPorProdutoDto(" +
            "p.id, p.nome, SUM(im.qtd), SUM(im.qtd * im.precoUnitario)) " +
            "FROM ItemMovimentacao im " +
            "JOIN im.movimentacao m " +
            "JOIN im.produto p " +
            "JOIN Venda v ON v.movimentacao = m " +
            "WHERE m.tipoMovimentacao = sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao.VENDA " +
            "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
            "AND (:idLoja IS NULL OR v.estabelecimento.id = :idLoja) " +
            "AND (:dataInicio IS NULL OR m.dataHora >= :dataInicio) " +
            "AND (:dataFim IS NULL OR m.dataHora <= :dataFim) " +
            "GROUP BY p.id, p.nome " +
            "ORDER BY SUM(im.qtd * im.precoUnitario) DESC")
    List<RelatorioVendasPorProdutoDto> relatorioVendasPorProduto(
            @Param("categoriaId") Integer categoriaId,
            @Param("idLoja") Integer idLoja,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}