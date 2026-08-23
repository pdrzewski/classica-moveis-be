package sptech.classicamoveis.Estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, EstoqueId> {

    List<Estoque> findById_EstabelecimentoId(Integer estabelecimentoId);
}