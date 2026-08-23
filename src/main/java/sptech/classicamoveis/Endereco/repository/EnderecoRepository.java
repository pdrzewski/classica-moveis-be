package sptech.classicamoveis.Endereco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Endereco.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}