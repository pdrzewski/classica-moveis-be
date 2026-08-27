package sptech.classicamoveis.Fornecedor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.service.EnderecoService;
import sptech.classicamoveis.Fornecedor.dto.*;
import sptech.classicamoveis.Fornecedor.mapper.FornecedorMapper;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceImplTest {
 @Mock FornecedorRepository repository; @Mock FornecedorMapper mapper; @Mock EnderecoService enderecoService; @InjectMocks FornecedorServiceImpl service;
 private Fornecedor fornecedor(long id){Fornecedor f=new Fornecedor();f.setId(id);f.setNome("Madeira");return f;} private FornecedorRequestDTO dto(){return new FornecedorRequestDTO("Madeira","1","João","1","2",3);} private FornecedorResponseDTO resposta(){return new FornecedorResponseDTO(1L,"Madeira","1","João","1","2",null);}
  @Test void listaFornecedores(){when(repository.findAll()).thenReturn(List.of(fornecedor(1)));when(mapper.toResponseDTO(any())).thenReturn(resposta());assertEquals(1,service.listarTodos().size());}
  @Test void buscaFornecedor(){Fornecedor f=fornecedor(1);when(repository.findById(1L)).thenReturn(Optional.of(f));when(mapper.toResponseDTO(f)).thenReturn(resposta());assertEquals("Madeira",service.buscarPorId(1L).nome());}
  @Test void falhaAoBuscarFornecedorAusente(){when(repository.findById(8L)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarPorId(8L));}
  @Test void atualizaFornecedor(){Fornecedor f=fornecedor(1);Endereco e=new Endereco();when(repository.findById(1L)).thenReturn(Optional.of(f));when(enderecoService.buscarEntidadePorId(3)).thenReturn(e);when(repository.save(f)).thenReturn(f);when(mapper.toResponseDTO(f)).thenReturn(resposta());service.atualizar(1L,dto());verify(mapper).updateEntityFromDto(dto(),e,f);}
  @Test void excluiFornecedor(){Fornecedor f=fornecedor(1);when(repository.findById(1L)).thenReturn(Optional.of(f));service.deletar(1L);verify(repository).delete(f);}
}
