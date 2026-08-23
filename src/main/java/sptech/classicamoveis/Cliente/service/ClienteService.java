package sptech.classicamoveis.Cliente.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.Mapper.ClienteMapper;
import sptech.classicamoveis.Cliente.dto.ClienteResponseDto;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;


    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteResponseDto> listarClientes() {
        List<Cliente> clientes = repository.findAll();
        return new ClienteMapper().toResponseDtoList(clientes);
    }

    public ClienteResponseDto criarCliente(Cliente cliente) {
        return new ClienteMapper().toResponseDto(repository.save(cliente));
    }

    public ClienteResponseDto buscarClientePorId(Integer id) {
        Cliente cliente = repository.findById(id).orElse(null);
        if (cliente != null) {
            return new ClienteMapper().toResponseDto(cliente);
        }
        return null;
    }

    public Cliente atualizarCliente(Integer id, Cliente clienteAtualizado) {
        Cliente clienteaAtualizar = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
            clienteaAtualizar.setNome(clienteAtualizado.getNome());
            clienteaAtualizar.setEnderecoId(clienteAtualizado.getEnderecoId());
            clienteaAtualizar.setDocumento(clienteAtualizado.getDocumento());
            clienteaAtualizar.setTelefone1(clienteAtualizado.getTelefone1());
            clienteaAtualizar.setTelefone2(clienteAtualizado.getTelefone2());
            clienteaAtualizar.setEmail(clienteAtualizado.getEmail());
            clienteaAtualizar.setObservacao(clienteAtualizado.getObservacao());
            clienteaAtualizar.setIe(clienteAtualizado.getIe());
            return repository.save(clienteaAtualizar);
    }

    public void deletarCliente(Integer id) {
        Cliente cliente = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        repository.delete(cliente);
    }

    public List<ClienteResponseDto> buscarClientesPorNome(String nome) {
        List<Cliente> clientes = repository.findByNomeContainingIgnoreCase(nome);
        return new ClienteMapper().toResponseDtoList(clientes);
    }

    public List<ClienteResponseDto> buscarClientesPorDocumento(String documento) {
        List<Cliente> clientes = repository.findAll().stream()
                .filter(cliente -> cliente.getDocumento() != null && cliente.getDocumento().contains(documento))
                .toList();
        return new ClienteMapper().toResponseDtoList(clientes);
    }

}
