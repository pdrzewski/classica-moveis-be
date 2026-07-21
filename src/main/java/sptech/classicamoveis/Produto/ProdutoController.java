package sptech.classicamoveis.Produto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Integer categoriaId) {
        return ResponseEntity.ok(service.buscarProdutos(nome, precoMax, categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarProdutoPorId(id));
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto produtoSalvo = service.salvarProduto(produto);

        // Retorna o status 201 Created junto com o cabeçalho Location apontando para a nova URI
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoSalvo.getId())
                .toUri();

        return ResponseEntity.created(uri).body(produtoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto produto) {
        produto.setId(id);
        return ResponseEntity.ok(service.salvarProduto(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}