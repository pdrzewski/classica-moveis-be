package sptech.classicamoveis.Usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLogin(String login);
}