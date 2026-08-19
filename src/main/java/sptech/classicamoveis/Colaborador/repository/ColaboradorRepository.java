package sptech.classicamoveis.Colaborador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.classicamoveis.Colaborador.model.Colaborador;

import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {

    Optional<Colaborador> findByUsuario_Login(String login);
}