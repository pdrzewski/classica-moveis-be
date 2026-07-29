package sptech.classicamoveis.Produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.classicamoveis.Produto.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
