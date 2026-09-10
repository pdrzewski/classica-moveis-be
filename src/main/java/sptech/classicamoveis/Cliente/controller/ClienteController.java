package sptech.classicamoveis.Cliente.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.dto.ClienteResponseDto;
import sptech.classicamoveis.Cliente.dto.ClienteComEnderecoRequestDto;
import sptech.classicamoveis.Cliente.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Cadastro e consulta de clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDto> buscarClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDto>> buscarClientesPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(clienteService.buscarClientesPorNome(nome));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDto> criarCliente(@RequestBody ClienteComEnderecoRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criarCliente(requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, clienteAtualizado));
    }

    @GetMapping("/documento/{documento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDto>> buscarClientesPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(clienteService.buscarClientesPorDocumento(documento));
    }
}
