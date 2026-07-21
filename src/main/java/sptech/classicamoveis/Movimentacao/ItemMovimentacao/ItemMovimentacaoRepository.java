package sptech.classicamoveis.Movimentacao.ItemMovimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ItemMovimentacaoRepository extends JpaRepository<ItemMovimentacao, Integer> {
    List<ItemMovimentacao> findByMovimentacaoId(Integer movimentacaoId);
}