package sptech.classicamoveis.Venda.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.classicamoveis.Venda.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
}
