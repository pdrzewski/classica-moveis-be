package sptech.classicamoveis.Relatorio;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/estoque")
    public ResponseEntity<List<RelatorioEstoqueItemDto>> relatorioEstoque(@RequestParam Integer idLoja) {
        return ResponseEntity.ok(relatorioService.relatorioEstoque(idLoja));
    }

    @GetMapping("/vendas-por-fornecedor")
    public ResponseEntity<List<RelatorioVendaItemDto>> relatorioVendasPorFornecedor(
            @RequestParam Integer fornecedorId,
            @RequestParam(required = false) Integer idLoja) {
        return ResponseEntity.ok(relatorioService.relatorioVendasPorFornecedor(fornecedorId, idLoja));
    }

    @GetMapping("/vendas-por-produto")
    public ResponseEntity<List<RelatorioVendasPorProdutoDto>> relatorioVendasPorProduto(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer idLoja,
            @RequestParam(required = false) List<Integer> produtoIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(relatorioService.relatorioVendasPorProduto(categoriaId, idLoja, produtoIds, dataInicio, dataFim));
    }
}