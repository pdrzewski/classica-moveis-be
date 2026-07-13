package sptech.classicamoveis.Colaborador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @GetMapping
    public ResponseEntity<List<ColaboradorResponseDto>> listarTodos() {
        return ResponseEntity.ok(colaboradorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponseDto> criar(@RequestBody  ColaboradorRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDto> atualizar(@PathVariable Integer id, @RequestBody  ColaboradorRequestDto dto) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        colaboradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
