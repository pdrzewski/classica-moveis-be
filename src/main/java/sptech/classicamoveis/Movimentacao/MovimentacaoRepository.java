package sptech.classicamoveis.Movimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {
    List<Movimentacao> findByTipoMovimentacaoAndStatus(TipoMovimentacao tipo, StatusMovimentacao status);
    
    List<Movimentacao> findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
            TipoMovimentacao tipo, StatusMovimentacao status, Integer estabelecimentoId);
    
    List<Movimentacao> findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
            TipoMovimentacao tipo, StatusMovimentacao status, Integer estabelecimentoId);

    List<Movimentacao> findByEstabelecimentoOrigemId(Integer estabelecimentoId);

    List<Movimentacao> findByEstabelecimentoDestinoId(Integer estabelecimentoId);

    @Query("""
        SELECT m
        FROM Movimentacao m
        WHERE m.tipoMovimentacao = :tipo
        AND m.colaborador.id = :colaboradorId
        AND m.visivel = true
        AND m.status <> :statusCancelado
        AND m.dataHora >= :inicio
        AND m.dataHora < :fim
        ORDER BY m.dataHora ASC
    """)
    List<Movimentacao> buscarVendasPorVendedor(
            @Param("colaboradorId") Integer colaboradorId,
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fim") java.time.LocalDateTime fim,
            @Param("tipo") TipoMovimentacao tipo,
            @Param("statusCancelado") StatusMovimentacao statusCancelado
    );

    @Query("""
    SELECT m
    FROM Movimentacao m
    WHERE m.visivel = true
    AND (:tipo IS NULL OR m.tipoMovimentacao = :tipo)
    AND (
        :estabelecimentoId IS NULL
        OR m.estabelecimentoOrigem.id = :estabelecimentoId
        OR m.estabelecimentoDestino.id = :estabelecimentoId
    )
    """)
    Page<Movimentacao> buscarHistorico(
            @Param("tipo") TipoMovimentacao tipo,
            @Param("estabelecimentoId") Integer estabelecimentoId,
            Pageable pageable
    );
}

