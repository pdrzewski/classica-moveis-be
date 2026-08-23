package sptech.classicamoveis.Cargo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.classicamoveis.Cargo.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {}
