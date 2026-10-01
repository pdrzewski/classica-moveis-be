package sptech.classicamoveis.Cargo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Cargo.dto.CargoPermissoesRequestDto;
import sptech.classicamoveis.Cargo.dto.CargoRequestDto;
import sptech.classicamoveis.Cargo.dto.CargoResponseDto;
import sptech.classicamoveis.Cargo.service.CargoService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
@Tag(name = "Cargos", description = "Cargos e suas permissões")
public class CargoController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<CargoResponseDto>> listarTodos() {
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @GetMapping("/{id}/permissoes")
    public ResponseEntity<CargoResponseDto> buscarPermissoesDoCargo(@PathVariable Integer id) {
        return ResponseEntity.ok(cargoService.buscarPermissoesPorId(id));
    }

    @PostMapping
    public ResponseEntity<CargoResponseDto> criar(@RequestBody CargoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseDto> atualizar(@PathVariable Integer id, @RequestBody CargoRequestDto dto) {
        return ResponseEntity.ok(cargoService.atualizar(id, dto));
    }

    @PutMapping("/{id}/permissoes")
    public ResponseEntity<CargoResponseDto> atualizarPermissoes(@PathVariable Integer id, @RequestBody CargoPermissoesRequestDto dto) {
        return ResponseEntity.ok(cargoService.atualizarPermissoes(id, dto == null ? Set.of() : dto.getPermissoesIds()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
