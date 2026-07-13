package sptech.classicamoveis.Permissao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissoes")
@RequiredArgsConstructor
public class PermissaoController {

    private final PermissaoService permissaoService;

    @GetMapping
    public ResponseEntity<List<PermissaoResponseDto>> listarTodos() {
        return ResponseEntity.ok(permissaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissaoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(permissaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PermissaoResponseDto> criar(@RequestBody PermissaoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissaoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponseDto> atualizar(@PathVariable Integer id, @RequestBody PermissaoRequestDto dto) {
        return ResponseEntity.ok(permissaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        permissaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
