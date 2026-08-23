package sptech.classicamoveis.Movimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;

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
}

