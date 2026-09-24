package sptech.classicamoveis.Colaborador.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Colaborador.AniversarioColaboradorDto;
import sptech.classicamoveis.Colaborador.FeriasRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colaboradores")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "Cadastro de colaboradores, férias e aniversariantes")
@PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<ColaboradorResponseDto> criar(@RequestBody ColaboradorRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDto> atualizar(@PathVariable Integer id, @RequestBody ColaboradorRequestDto dto) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        colaboradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ferias")
    public ResponseEntity<ColaboradorResponseDto> registrarFerias(@PathVariable Integer id, @RequestBody FeriasRequestDto dto) {
        return ResponseEntity.ok(colaboradorService.registrarFerias(id, dto));
    }

    @PatchMapping("/{id}/ferias/encerrar")
    public ResponseEntity<ColaboradorResponseDto> encerrarFerias(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.encerrarFerias(id));
    }

    @GetMapping("/aniversarios-proximos")
    public ResponseEntity<List<AniversarioColaboradorDto>> aniversariosProximos(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(colaboradorService.buscarAniversariosProximos(dias));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensagem", "Já existe um registro duplicado para este usuário ou CPF.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @GetMapping("/ferias")
    public ResponseEntity<List<ColaboradorResponseDto>> listarEmFerias() {
        return ResponseEntity.ok(
                colaboradorService.listarEmFerias()
        );
    }
}