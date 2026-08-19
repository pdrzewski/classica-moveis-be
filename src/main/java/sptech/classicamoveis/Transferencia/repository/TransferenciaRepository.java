package sptech.classicamoveis.Transferencia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Transferencia.model.Transferencia;


@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Integer> {
}
