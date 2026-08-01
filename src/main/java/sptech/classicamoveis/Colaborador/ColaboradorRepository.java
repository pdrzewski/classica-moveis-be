package sptech.classicamoveis.Colaborador;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {

    Optional<Colaborador> findByUsuario_Login(String login);
}