package sptech.classicamoveis.Cliente.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.dto.ClienteResponseDto;
import sptech.classicamoveis.Cliente.service.ClienteService;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }


    @GetMapping("/{nome}")
    public ResponseEntity<List<ClienteResponseDto>> buscarClientesPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(clienteService.buscarClientesPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> criarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.criarCliente(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, clienteAtualizado));
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<List<ClienteResponseDto>> buscarClientesPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(clienteService.buscarClientesPorDocumento(documento));
    }

}
