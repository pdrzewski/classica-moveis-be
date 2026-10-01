package sptech.classicamoveis.Movimentacao.Pagamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.classicamoveis.Movimentacao.Pagamento.Pagamento;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByMovimentacaoId(Integer movimentacaoId);
}