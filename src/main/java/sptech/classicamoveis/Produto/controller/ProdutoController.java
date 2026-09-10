package sptech.classicamoveis.Produto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Produto.dto.ProdutoEstoqueBaixoDTO;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;
import sptech.classicamoveis.Produto.service.ProdutoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Catálogo de produtos: cadastro, consulta e controle de preços/estoque mínimo")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorTermo(@RequestParam(value = "q", required = false) String q) {
        return ResponseEntity.ok(produtoService.buscarPorTermo(q));
    }

    @Operation(
            summary = "Lista produtos abaixo do estoque mínimo",
            description = "Para cada produto, soma o estoque atual (calculado pelo EstoqueService, mesmo cálculo " +
                    "usado no módulo de Estoque) em todos os estabelecimentos e retorna os produtos cujo total " +
                    "é menor ou igual ao estoque mínimo cadastrado. Não filtra por estabelecimento."
    )
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoEstoqueBaixoDTO>> listarProdutosAbaixoDoEstoqueMinimo() {
        return ResponseEntity.ok(produtoService.listarProdutosAbaixoDoEstoqueMinimo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO criado = produtoService.criar(dto);
        return ResponseEntity.created(URI.create("/api/produtos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Integer id,
                                                        @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}