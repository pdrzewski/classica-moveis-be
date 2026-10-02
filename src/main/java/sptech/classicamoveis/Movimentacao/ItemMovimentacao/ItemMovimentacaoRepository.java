package sptech.classicamoveis.Movimentacao.ItemMovimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMovimentacaoRepository
        extends JpaRepository<ItemMovimentacao, Integer> {

    List<ItemMovimentacao> findByMovimentacaoId(
            Integer movimentacaoId
    );

    @Query("""
        SELECT im
        FROM ItemMovimentacao im
        WHERE im.produto.id = :produtoId
    """)
    List<ItemMovimentacao> findByProdutoId(
            @Param("produtoId") Integer produtoId
    );

    @Query("""
        SELECT im
        FROM ItemMovimentacao im
        WHERE im.produto.id = :produtoId
        AND (
            im.movimentacao.estabelecimentoOrigem.id = :estabelecimentoId
            OR
            im.movimentacao.estabelecimentoDestino.id = :estabelecimentoId
        )
    """)
    List<ItemMovimentacao> findByProdutoIdAndEstabelecimentoId(
            @Param("produtoId") Integer produtoId,
            @Param("estabelecimentoId") Integer estabelecimentoId
    );
}