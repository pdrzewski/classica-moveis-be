package sptech.classicamoveis.Movimentacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/{estabelecimentoId}/produtos/{produtoId}")
    public ResponseEntity<Map<String, Object>> buscarSaldo(
            @PathVariable Integer estabelecimentoId,
            @PathVariable Integer produtoId) {
        Long saldo = estoqueService.calcularSaldoProduto(estabelecimentoId, produtoId);
        Map<String, Object> response = new HashMap<>();
        response.put("estabelecimentoId", estabelecimentoId);
        response.put("produtoId", produtoId);
        response.put("saldo", saldo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{estabelecimentoId}")
    public ResponseEntity<Map<String, Object>> buscarInventarioCompleto(
            @PathVariable Integer estabelecimentoId) {
        Map<Integer, Long> inventario = estoqueService.calcularInventarioCompleto(estabelecimentoId);
        Map<String, Object> response = new HashMap<>();
        response.put("estabelecimentoId", estabelecimentoId);
        response.put("inventario", inventario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{estabelecimentoId}/extrato/{produtoId}")
    public ResponseEntity<Map<String, Object>> buscarExtratoMovimentacoes(
            @PathVariable Integer estabelecimentoId,
            @PathVariable Integer produtoId) {
        Map<String, Object> extrato = estoqueService.obterExtratoMovimentacoes(estabelecimentoId, produtoId);
        return ResponseEntity.ok(extrato);
    }
}

