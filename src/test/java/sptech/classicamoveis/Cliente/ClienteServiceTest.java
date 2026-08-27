package sptech.classicamoveis.Cliente;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Cliente.dto.ClienteComEnderecoRequestDto;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;
import sptech.classicamoveis.Cliente.service.ClienteService;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock ClienteRepository repository;
    @Mock EnderecoRepository enderecoRepository;
    @InjectMocks ClienteService service;

    private Cliente cliente(int id,String nome,String doc){
        Cliente c=new Cliente(); c.setId(id); c.setNome(nome); c.setDocumento(doc); c.setTelefone1("11999999999"); c.setTelefone2("11888888888"); c.setEmail("a@a.com"); c.setObservacao("obs"); c.setIe("IE");
        Endereco e=new Endereco(); e.setId(id); e.setCep("01000-000"); e.setLogradouro("Rua A"); e.setBairro("Centro"); e.setCidade("SP"); e.setNumero("10"); e.setEstado("SP"); c.setEndereco(e); return c;
    }
    @Test void listar(){when(repository.findAll()).thenReturn(List.of(cliente(1,"Ana","111")));assertEquals("Ana",service.listarClientes().get(0).getNome());}
    @Test void buscarExistente(){when(repository.findById(1)).thenReturn(Optional.of(cliente(1,"Ana","111")));assertEquals("Ana",service.buscarClientePorId(1).getNome());}
    @Test void buscarInexistenteRetornaNull(){when(repository.findById(9)).thenReturn(Optional.empty());assertNull(service.buscarClientePorId(9));}
    @Test void criar(){
        ClienteComEnderecoRequestDto d=new ClienteComEnderecoRequestDto("Ana","111","119","118","a@a.com","obs","IE","01000","Rua","Centro","SP","10",null,"SP");
        Endereco e=new Endereco(); e.setId(7); when(enderecoRepository.save(any())).thenReturn(e); when(repository.save(any())).thenAnswer(i->{Cliente c=i.getArgument(0);c.setId(1);return c;});
        var r=service.criarCliente(d); assertEquals("Ana",r.getNome()); verify(enderecoRepository).save(any(Endereco.class)); verify(repository).save(any(Cliente.class));
    }
    @Test void atualizar(){
        Cliente atual=cliente(1,"Old","1"); Cliente novo=cliente(2,"New","2"); when(repository.findById(1)).thenReturn(Optional.of(atual)); when(repository.save(atual)).thenReturn(atual);
        assertEquals("New",service.atualizarCliente(1,novo).getNome()); verify(repository).save(atual);
    }
    @Test void atualizarInexistente(){when(repository.findById(9)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.atualizarCliente(9,cliente(1,"x","x")));}
    @Test void deletar(){Cliente c=cliente(1,"Ana","1");when(repository.findById(1)).thenReturn(Optional.of(c));service.deletarCliente(1);verify(repository).delete(c);}
    @Test void deletarInexistente(){when(repository.findById(9)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.deletarCliente(9));}
    @Test void buscarNome(){when(repository.findByNomeContainingIgnoreCase("ana")).thenReturn(List.of(cliente(1,"Ana","1")));assertEquals(1,service.buscarClientesPorNome("ana").size());}
    @Test void buscarDocumentoIgnoraNulo(){Cliente a=cliente(1,"Ana","123");Cliente b=cliente(2,"Bob",null);when(repository.findAll()).thenReturn(List.of(a,b));assertEquals(1,service.buscarClientesPorDocumento("23").size());}
}
