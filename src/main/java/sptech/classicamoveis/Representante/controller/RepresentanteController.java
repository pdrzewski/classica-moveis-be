package sptech.classicamoveis.Representante.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Representante.dto.RepresentanteRequestDTO;
import sptech.classicamoveis.Representante.dto.RepresentanteResponseDTO;
import sptech.classicamoveis.Representante.service.RepresentanteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/representantes")
@RequiredArgsConstructor
@Tag(name = "Representantes", description = "Representantes e fornecedores que representam")
public class RepresentanteController {

    private final RepresentanteService representanteService;

    @GetMapping
    public ResponseEntity<List<RepresentanteResponseDTO>> listar(
            @RequestParam(required = false) String termo) {
        return ResponseEntity.ok(representanteService.listar(termo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(representanteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RepresentanteResponseDTO> criar(
            @Valid @RequestBody RepresentanteRequestDTO dto) {
        RepresentanteResponseDTO criado = representanteService.criar(dto);
        return ResponseEntity.created(URI.create("/representantes/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RepresentanteRequestDTO dto) {
        return ResponseEntity.ok(representanteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        representanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
