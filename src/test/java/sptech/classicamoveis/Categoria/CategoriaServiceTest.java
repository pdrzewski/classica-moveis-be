package sptech.classicamoveis.Categoria;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Categoria.dto.CategoriaRequestDto;
import sptech.classicamoveis.Categoria.dto.CategoriaResponseDto;
import sptech.classicamoveis.Categoria.service.CategoriaService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
    @Mock private sptech.classicamoveis.Categoria.repository.CategoriaRepository repository;
    @InjectMocks private CategoriaService service;

    @Test void listarTodos(){
        var a=new sptech.classicamoveis.Categoria.Categoria(1,"Sala");
        var b=new sptech.classicamoveis.Categoria.Categoria(2,"Quarto");
        when(repository.findAll()).thenReturn(List.of(a,b));
        var r=service.listarTodos();
        assertEquals(2,r.size()); assertEquals("Sala",r.get(0).getNome());
    }
    @Test void buscarPorId(){
        var c=new sptech.classicamoveis.Categoria.Categoria(1,"Sala");
        when(repository.findById(1)).thenReturn(Optional.of(c));
        assertEquals("Sala",service.buscarPorId(1).getCategoria());
    }
    @Test void buscarInexistente(){
        when(repository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->service.buscarPorId(9));
    }
    @Test void criarComCategoria(){
        when(repository.save(any())).thenAnswer(i->{var c=(sptech.classicamoveis.Categoria.Categoria)i.getArgument(0);c.setId(3);return c;});
        assertEquals("Mesa", service.criar(new CategoriaRequestDto("  Mesa  ")).getNome());
    }
    @Test void criarUsaAliasNome(){
        var dto=new CategoriaRequestDto(); dto.setNome("Escritorio");
        when(repository.save(any())).thenAnswer(i->i.getArgument(0));
        assertEquals("Escritorio",service.criar(dto).getNome());
    }
    @Test void rejeitaDtoNulo(){ assertThrows(IllegalArgumentException.class,()->service.criar(null)); }
    @Test void rejeitaNomeVazio(){ assertThrows(IllegalArgumentException.class,()->service.criar(new CategoriaRequestDto("   "))); }
    @Test void atualiza(){
        var c=new sptech.classicamoveis.Categoria.Categoria(1,"Antiga");
        when(repository.findById(1)).thenReturn(Optional.of(c)); when(repository.save(c)).thenReturn(c);
        assertEquals("Nova",service.atualizar(1,new CategoriaRequestDto("Nova")).getNome());
    }
    @Test void deletar(){
        var c=new sptech.classicamoveis.Categoria.Categoria(1,"Sala"); when(repository.findById(1)).thenReturn(Optional.of(c));
        service.deletar(1); verify(repository).delete(c);
    }
}
