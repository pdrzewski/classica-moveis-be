package sptech.classicamoveis.Movimentacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoResponseDto;
import sptech.classicamoveis.Movimentacao.service.MovimentacaoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoResponseDto> criar(@RequestBody MovimentacaoRequestDto requestDto) {
        return ResponseEntity.ok(movimentacaoService.criarMovimentacao(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(movimentacaoService.buscarPorId(id));
    }

    @GetMapping("/vendas-pendentes")
    public ResponseEntity<List<MovimentacaoResponseDto>> buscarVendasPendentes(
            @RequestParam(required = false) Integer estabelecimentoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {
        return ResponseEntity.ok(movimentacaoService.buscarVendasPendentes(estabelecimentoId, dataInicio, dataFim));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<MovimentacaoResponseDto> concluir(@PathVariable Integer id) {
        return ResponseEntity.ok(movimentacaoService.concluir(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<MovimentacaoResponseDto> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(movimentacaoService.cancelar(id));
    }
}

