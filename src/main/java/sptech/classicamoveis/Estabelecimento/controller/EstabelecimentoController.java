package sptech.classicamoveis.Estabelecimento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;
import sptech.classicamoveis.Estabelecimento.service.EstabelecimentoService;

import java.util.List;

@RestController
@RequestMapping("/estabelecimentos")
@RequiredArgsConstructor
public class EstabelecimentoController {

    private final EstabelecimentoService estabelecimentoService;

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDto>> listarTodos() {
        return ResponseEntity.ok(estabelecimentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(estabelecimentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDto> criar(@RequestBody EstabelecimentoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estabelecimentoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> atualizar(@PathVariable Integer id, @RequestBody EstabelecimentoRequestDto dto) {
        return ResponseEntity.ok(estabelecimentoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        estabelecimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
