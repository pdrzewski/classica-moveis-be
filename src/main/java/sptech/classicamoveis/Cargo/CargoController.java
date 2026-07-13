package sptech.classicamoveis.Cargo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
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

    @PostMapping
    public ResponseEntity<CargoResponseDto> criar(@RequestBody CargoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseDto> atualizar(@PathVariable Integer id, @RequestBody CargoRequestDto dto) {
        return ResponseEntity.ok(cargoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
