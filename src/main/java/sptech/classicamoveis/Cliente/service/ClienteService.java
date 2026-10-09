package sptech.classicamoveis.Cliente.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.Mapper.ClienteMapper;
import sptech.classicamoveis.Cliente.dto.ClienteResponseDto;
import sptech.classicamoveis.Cliente.dto.ClienteComEnderecoRequestDto;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository repository;
    private final EnderecoRepository enderecoRepository;


    public ClienteService(ClienteRepository repository, EnderecoRepository enderecoRepository) {
        this.repository = repository;
        this.enderecoRepository = enderecoRepository;
    }

    public List<ClienteResponseDto> listarClientes() {
        List<Cliente> clientes = repository.findAll();
        return new ClienteMapper().toResponseDtoList(clientes);
    }

    public ClienteResponseDto criarCliente(ClienteComEnderecoRequestDto requestDto) {
        Endereco endereco = new Endereco();
        endereco.setCep(normalizarCep(requestDto.getCep()));
        endereco.setLogradouro(requestDto.getLogradouro());
        endereco.setBairro(requestDto.getBairro());
        endereco.setCidade(requestDto.getCidade());
        endereco.setNumero(requestDto.getNumero());
        endereco.setComplemento(requestDto.getComplemento());
        endereco.setEstado(requestDto.getEstado());
        
        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        Cliente cliente = new Cliente();
        cliente.setNome(requestDto.getNome());
        cliente.setDocumento(requestDto.getDocumento());
        cliente.setTelefone1(requestDto.getTelefone1());
        cliente.setTelefone2(requestDto.getTelefone2());
        cliente.setEmail(requestDto.getEmail());
        cliente.setObservacao(requestDto.getObservacao());
        cliente.setIe(requestDto.getIe());
        cliente.setEndereco(enderecoSalvo);
        
        return new ClienteMapper().toResponseDto(repository.save(cliente));
    }

    public ClienteResponseDto buscarClientePorId(Integer id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));
        return new ClienteMapper().toResponseDto(cliente);
    }

    public ClienteResponseDto atualizarCliente(Integer id, ClienteComEnderecoRequestDto dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));

        cliente.setNome(dto.getNome());
        cliente.setDocumento(dto.getDocumento());
        cliente.setTelefone1(dto.getTelefone1());
        cliente.setTelefone2(dto.getTelefone2());
        cliente.setEmail(dto.getEmail());
        cliente.setObservacao(dto.getObservacao());
        cliente.setIe(dto.getIe());

        Endereco endereco = cliente.getEndereco();
        endereco.setCep(normalizarCep(dto.getCep()));
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setEstado(dto.getEstado());
        enderecoRepository.save(endereco);

        return new ClienteMapper().toResponseDto(repository.save(cliente));
    }

    private String normalizarCep(String cep) {
        if (cep == null) return null;
        String numeros = cep.replaceAll("\\D", "");
        if (numeros.length() != 8) throw new IllegalArgumentException("CEP deve conter 8 números.");
        return numeros;
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
