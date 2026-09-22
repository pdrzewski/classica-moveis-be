package sptech.classicamoveis.Movimentacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Movimentacao.dto.EstoqueProdutoDto;
import sptech.classicamoveis.Movimentacao.dto.InventarioContagemRequestDto;
import sptech.classicamoveis.Movimentacao.dto.InventarioProdutoDto;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estoque")
@Tag(
        name = "Estoque",
        description = "Consulta de saldo, inventário e extrato de movimentações por estabelecimento"
)
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/{estabelecimentoId}/produtos/{produtoId}")
    public ResponseEntity<Map<String, Object>> buscarSaldo(
            @PathVariable Integer estabelecimentoId,
            @PathVariable Integer produtoId) {

        Long saldo = estoqueService.calcularSaldoProduto(
                estabelecimentoId,
                produtoId
        );

        Map<String, Object> response = new HashMap<>();

        response.put("estabelecimentoId", estabelecimentoId);
        response.put("produtoId", produtoId);
        response.put("saldo", saldo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{estabelecimentoId}")
    public ResponseEntity<Map<String, Object>> buscarInventarioCompleto(
            @PathVariable Integer estabelecimentoId) {

        Map<Integer, Long> inventario =
                estoqueService.calcularInventarioCompleto(
                        estabelecimentoId
                );

        Map<String, Object> response = new HashMap<>();

        response.put("estabelecimentoId", estabelecimentoId);
        response.put("inventario", inventario);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{estabelecimentoId}/detalhado")
    public ResponseEntity<List<EstoqueProdutoDto>> buscarEstoqueDetalhado(
            @PathVariable Integer estabelecimentoId) {

        return ResponseEntity.ok(
                estoqueService.buscarEstoquePorEstabelecimento(
                        estabelecimentoId
                )
        );
    }

    /*
     * ============================================================
     * INVENTÁRIO
     * ============================================================
     */

    @Operation(
            summary = "Lista os produtos para a tela de inventário"
    )
    @GetMapping("/{estabelecimentoId}/inventario")
    public ResponseEntity<List<InventarioProdutoDto>> buscarInventario(
            @PathVariable Integer estabelecimentoId) {

        return ResponseEntity.ok(
                estoqueService.buscarInventario(estabelecimentoId)
        );
    }

    @Operation(
            summary = "Registra a quantidade física contada no inventário"
    )
    @PutMapping("/{estabelecimentoId}/inventario/{produtoId}")
    public ResponseEntity<InventarioProdutoDto> registrarContagem(
            @PathVariable Integer estabelecimentoId,
            @PathVariable Integer produtoId,
            @Valid @RequestBody InventarioContagemRequestDto request) {

        InventarioProdutoDto response =
                estoqueService.registrarContagem(
                        estabelecimentoId,
                        produtoId,
                        request.quantidadeContada(),
                        request.colaboradorId()
                );

        return ResponseEntity.ok(response);
    }

    /*
     * ============================================================
     * EXTRATO
     * ============================================================
     */

    @GetMapping("/{estabelecimentoId}/extrato/{produtoId}")
    public ResponseEntity<Map<String, Object>> buscarExtratoMovimentacoes(
            @PathVariable Integer estabelecimentoId,
            @PathVariable Integer produtoId) {

        Map<String, Object> extrato =
                estoqueService.obterExtratoMovimentacoes(
                        estabelecimentoId,
                        produtoId
                );

        return ResponseEntity.ok(extrato);
    }
}