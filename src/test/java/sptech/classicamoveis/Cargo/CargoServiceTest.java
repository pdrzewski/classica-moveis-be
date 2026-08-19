package sptech.classicamoveis.Cargo;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Permissao.model.Permissao;
import sptech.classicamoveis.Permissao.repository.PermissaoRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {
 @Mock CargoRepository repository; @Mock PermissaoRepository permissaoRepository; @InjectMocks CargoService service;
 private Cargo cargo(int id, String nome) { Cargo c=new Cargo(); c.setId(id); c.setCargo(nome); return c; }
 @Test void listaCargos() { when(repository.findAll()).thenReturn(List.of(cargo(1,"GERENTE"))); assertEquals("GERENTE",service.listarTodos().getFirst().getNome()); }
 @Test void buscaCargo() { when(repository.findById(1)).thenReturn(Optional.of(cargo(1,"GERENTE"))); assertEquals(1,service.buscarPorId(1).getId()); }
 @Test void falhaAoBuscarCargoInexistente() { when(repository.findById(8)).thenReturn(Optional.empty()); assertThrows(EntityNotFoundException.class,()->service.buscarPorId(8)); }
 @Test void criaCargo() { when(repository.save(any())).thenAnswer(i->{Cargo c=i.getArgument(0);c.setId(2);return c;}); assertEquals("CAIXA",service.criar(new CargoRequestDto("CAIXA",Set.of())).getNome()); }
 @Test void criaCargoComPermissao() { Permissao p=new Permissao(5,"VENDER"); when(permissaoRepository.findById(5)).thenReturn(Optional.of(p)); when(repository.save(any())).thenAnswer(i->i.getArgument(0)); assertTrue(service.criar(new CargoRequestDto("CAIXA",Set.of(5))).getPermissoes().contains("VENDER")); }
 @Test void falhaAoCriarComPermissaoInexistente() { when(permissaoRepository.findById(5)).thenReturn(Optional.empty()); assertThrows(EntityNotFoundException.class,()->service.criar(new CargoRequestDto("CAIXA",Set.of(5)))); }
 @Test void atualizaCargo() { Cargo c=cargo(1,"CAIXA"); when(repository.findById(1)).thenReturn(Optional.of(c)); when(repository.save(c)).thenReturn(c); assertEquals("GERENTE",service.atualizar(1,new CargoRequestDto("GERENTE",null)).getNome()); }
 @Test void excluiCargo() { Cargo c=cargo(1,"CAIXA");when(repository.findById(1)).thenReturn(Optional.of(c));service.deletar(1);verify(repository).delete(c); }
}
