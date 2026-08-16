package sptech.classicamoveis.Endereco.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
 @Mock EnderecoRepository repository; @InjectMocks EnderecoService service;
 private Endereco endereco(int id){ Endereco e=new Endereco();e.setId(id);e.setCidade("São Paulo");return e; }
 @Test void buscaEnderecoExistente(){when(repository.findById(1)).thenReturn(Optional.of(endereco(1)));assertEquals("São Paulo",service.buscarEntidadePorId(1).getCidade());}
 @Test void consultaRepositorioComIdSolicitado(){when(repository.findById(22)).thenReturn(Optional.of(endereco(22)));service.buscarEntidadePorId(22);verify(repository).findById(22);}
 @Test void mantemIdentidadeDaEntidade(){Endereco e=endereco(3);when(repository.findById(3)).thenReturn(Optional.of(e));assertSame(e,service.buscarEntidadePorId(3));}
 @Test void falhaComIdInexistente(){when(repository.findById(9)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarEntidadePorId(9));}
 @Test void informaIdNaMensagemDeErro(){when(repository.findById(9)).thenReturn(Optional.empty());assertTrue(assertThrows(EntityNotFoundException.class,()->service.buscarEntidadePorId(9)).getMessage().contains("9"));}
}
