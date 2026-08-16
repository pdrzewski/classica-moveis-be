package sptech.classicamoveis.Colaborador;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Cargo.*;
import sptech.classicamoveis.Usuario.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceTest {
 @Mock ColaboradorRepository repository; @Mock CargoRepository cargoRepository; @Mock UsuarioRepository usuarioRepository; @InjectMocks ColaboradorService service;
 private ColaboradorRequestDto dto(Boolean ferias){return new ColaboradorRequestDto("Ana",2,3,ferias,LocalDate.of(2020,1,1),LocalDate.of(1990,1,1),2000.0,"CT",10);} private Cargo cargo(){return new Cargo(2,"GERENTE",new HashSet<>());} private Usuario usuario(){return new Usuario(3,"ana","h");} private Colaborador colaborador(){Colaborador c=new Colaborador();c.setId(1);c.setNome("Ana");c.setCargo(cargo());c.setUsuario(usuario());return c;}
 @Test void listaColaboradores(){when(repository.findAll()).thenReturn(List.of(colaborador()));assertEquals("Ana",service.listarTodos().getFirst().getNome());}
 @Test void buscaColaborador(){when(repository.findById(1)).thenReturn(Optional.of(colaborador()));assertEquals(2,service.buscarPorId(1).getCargoId());}
 @Test void falhaAoBuscarColaboradorAusente(){when(repository.findById(9)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarPorId(9));}
 @Test void criaColaboradorEAssumeFeriasFalso(){when(cargoRepository.findById(2)).thenReturn(Optional.of(cargo()));when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario()));when(repository.save(any())).thenAnswer(i->{Colaborador c=i.getArgument(0);c.setId(1);return c;});assertFalse(service.criar(dto(null)).getEmFerias());}
 @Test void falhaAoCriarSemCargo(){when(cargoRepository.findById(2)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.criar(dto(true)));verifyNoInteractions(usuarioRepository);}
 @Test void falhaAoCriarSemUsuario(){when(cargoRepository.findById(2)).thenReturn(Optional.of(cargo()));when(usuarioRepository.findById(3)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.criar(dto(true)));}
 @Test void atualizaColaborador(){Colaborador c=colaborador();when(repository.findById(1)).thenReturn(Optional.of(c));when(cargoRepository.findById(2)).thenReturn(Optional.of(cargo()));when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario()));when(repository.save(c)).thenReturn(c);assertTrue(service.atualizar(1,dto(true)).getEmFerias());}
 @Test void excluiColaborador(){Colaborador c=colaborador();when(repository.findById(1)).thenReturn(Optional.of(c));service.deletar(1);verify(repository).delete(c);}
}
