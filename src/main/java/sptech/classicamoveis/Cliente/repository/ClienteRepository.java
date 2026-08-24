package sptech.classicamoveis.Cliente.repository;

import sptech.classicamoveis.Cliente.Cliente;

import java.util.List;

public interface ClienteRepository extends org.springframework.data.jpa.repository.JpaRepository<sptech.classicamoveis.Cliente.Cliente, Integer> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
