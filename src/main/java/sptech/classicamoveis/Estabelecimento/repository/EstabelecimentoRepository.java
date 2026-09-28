package sptech.classicamoveis.Estabelecimento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Integer> {

    Optional<Estabelecimento> findFirstByNomeContainingIgnoreCase(String termo);
}
