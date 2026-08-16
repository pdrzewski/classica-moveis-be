package sptech.classicamoveis.Usuario;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
 @Mock UsuarioRepository repository; @Mock PasswordEncoder encoder; @InjectMocks UsuarioService service;
 private Usuario usuario(int id,String login){return new Usuario(id,login,"hash");}
 @Test void listaUsuarios(){when(repository.findAll()).thenReturn(List.of(usuario(1,"ana")));assertEquals("ana",service.listarTodos().getFirst().getLogin());}
 @Test void buscaUsuario(){when(repository.findById(1)).thenReturn(Optional.of(usuario(1,"ana")));assertEquals(1,service.buscarPorId(1).getId());}
 @Test void falhaAoBuscarUsuarioAusente(){when(repository.findById(1)).thenReturn(Optional.empty());assertThrows(EntityNotFoundException.class,()->service.buscarPorId(1));}
 @Test void criaUsuarioComSenhaCodificada(){when(repository.findByLogin("ana")).thenReturn(Optional.empty());when(encoder.encode("123")).thenReturn("hash");when(repository.save(any())).thenAnswer(i->{Usuario u=i.getArgument(0);u.setId(1);return u;});assertEquals("ana",service.criar(new UsuarioRequestDto("ana","123")).getLogin());verify(encoder).encode("123");}
 @Test void impedeLoginDuplicado(){when(repository.findByLogin("ana")).thenReturn(Optional.of(usuario(1,"ana")));assertThrows(EntityExistsException.class,()->service.criar(new UsuarioRequestDto("ana","123")));verify(repository,never()).save(any());}
 @Test void excluiUsuario(){Usuario u=usuario(1,"ana");when(repository.findById(1)).thenReturn(Optional.of(u));service.deletar(1);verify(repository).delete(u);}
}
