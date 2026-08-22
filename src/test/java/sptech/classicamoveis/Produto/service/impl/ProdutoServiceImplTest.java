package sptech.classicamoveis.Produto.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Categoria.repository.CategoriaRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Produto.dto.*;
import sptech.classicamoveis.Produto.exception.RecursoNaoEncontradoException;
import sptech.classicamoveis.Produto.mapper.ProdutoMapper;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {
 @Mock ProdutoRepository repository; @Mock FornecedorRepository fornecedorRepository; @Mock CategoriaRepository categoriaRepository; @Mock ProdutoMapper mapper; @InjectMocks ProdutoServiceImpl service;
 private ProdutoRequestDTO dto(){return new ProdutoRequestDTO(2L,3,"Mesa","SKU","BAR","UN","Marca",10.0,20.0,1,true);} private Produto produto(){Produto p=new Produto();p.setId(1);return p;} private ProdutoResponseDTO response(){return new ProdutoResponseDTO(1,2L,3,"Mesa","SKU","BAR","UN","Marca",10.0,20.0,1,true);}
 @Test void listaProdutos(){Produto p=produto();when(repository.findAll()).thenReturn(List.of(p));when(mapper.toResponseDTO(p)).thenReturn(response());assertEquals(1,service.listarTodos().size());}
 @Test void buscaProduto(){Produto p=produto();when(repository.findById(1)).thenReturn(Optional.of(p));when(mapper.toResponseDTO(p)).thenReturn(response());assertEquals("Mesa",service.buscarPorId(1).nome());}
 @Test void falhaAoBuscarProdutoAusente(){when(repository.findById(1)).thenReturn(Optional.empty());assertThrows(RecursoNaoEncontradoException.class,()->service.buscarPorId(1));}
 @Test void criaProduto(){Fornecedor f=new Fornecedor();Categoria c=new Categoria();Produto p=produto();when(fornecedorRepository.findById(2L)).thenReturn(Optional.of(f));when(categoriaRepository.findById(3)).thenReturn(Optional.of(c));when(mapper.toEntity(dto(),f,c)).thenReturn(p);when(repository.save(p)).thenReturn(p);when(mapper.toResponseDTO(p)).thenReturn(response());assertEquals(1,service.criar(dto()).id());}
 @Test void falhaAoCriarSemFornecedor(){when(fornecedorRepository.findById(2L)).thenReturn(Optional.empty());assertThrows(RecursoNaoEncontradoException.class,()->service.criar(dto()));verifyNoInteractions(categoriaRepository);}
 @Test void falhaAoCriarSemCategoria(){when(fornecedorRepository.findById(2L)).thenReturn(Optional.of(new Fornecedor()));when(categoriaRepository.findById(3)).thenReturn(Optional.empty());assertThrows(RecursoNaoEncontradoException.class,()->service.criar(dto()));}
 @Test void atualizaProduto(){Produto p=produto();Fornecedor f=new Fornecedor();Categoria c=new Categoria();when(repository.findById(1)).thenReturn(Optional.of(p));when(fornecedorRepository.findById(2L)).thenReturn(Optional.of(f));when(categoriaRepository.findById(3)).thenReturn(Optional.of(c));when(repository.save(p)).thenReturn(p);when(mapper.toResponseDTO(p)).thenReturn(response());service.atualizar(1,dto());verify(mapper).preencherEntidade(p,dto(),f,c);}
 @Test void excluiProduto(){Produto p=produto();when(repository.findById(1)).thenReturn(Optional.of(p));service.deletar(1);verify(repository).delete(p);}
}
