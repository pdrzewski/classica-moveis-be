package sptech.classicamoveis.Representante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.classicamoveis.Representante.model.Representante;

import java.util.List;

public interface RepresentanteRepository extends JpaRepository<Representante, Long> {

    @Query("""
            SELECT DISTINCT r
            FROM Representante r
            LEFT JOIN r.fornecedores f
            WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    List<Representante> buscarPorTermo(@Param("termo") String termo);
}
