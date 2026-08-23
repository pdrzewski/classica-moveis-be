package sptech.classicamoveis.Produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.classicamoveis.Produto.model.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	@Query("SELECT DISTINCT p FROM Produto p LEFT JOIN p.fornecedor f LEFT JOIN p.categoria c " +
			"WHERE (LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(c.categoria) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :termo, '%')) " +
			"OR LOWER(p.unidadeMedida) LIKE LOWER(CONCAT('%', :termo, '%')))")
	List<Produto> searchByTerm(@Param("termo") String termo);

	List<Produto> findByPrecoCustoEqualsOrPrecoVendaEquals(Double precoCusto, Double precoVenda);

	List<Produto> findByIdEqualsOrEstoqueMinimoEquals(Integer id, Integer estoqueMinimo);
}
