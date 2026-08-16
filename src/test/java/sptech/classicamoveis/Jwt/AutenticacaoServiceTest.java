package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sptech.classicamoveis.Colaborador.Colaborador;
import sptech.classicamoveis.Colaborador.ColaboradorRepository;
import sptech.classicamoveis.Usuario.Usuario;
import sptech.classicamoveis.Usuario.UsuarioRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
 @Mock UsuarioRepository usuarioRepository; @Mock ColaboradorRepository colaboradorRepository; @InjectMocks AutenticacaoService service;
 private Usuario usuario(){return new Usuario(1,"ana","hash");}
 @Test void carregaUsuarioAutenticado(){when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario()));when(colaboradorRepository.findByUsuario_Login("ana")).thenReturn(Optional.empty());assertEquals("ana",service.loadUserByUsername("ana").getUsername());}
 @Test void buscaColaboradorDoUsuario(){when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario()));when(colaboradorRepository.findByUsuario_Login("ana")).thenReturn(Optional.empty());service.loadUserByUsername("ana");verify(colaboradorRepository).findByUsuario_Login("ana");}
 @Test void aceitaUsuarioSemColaborador(){when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario()));when(colaboradorRepository.findByUsuario_Login("ana")).thenReturn(Optional.empty());assertNotNull(service.loadUserByUsername("ana"));}
 @Test void retornaAutoridadesVaziasSemColaborador(){when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario()));when(colaboradorRepository.findByUsuario_Login("ana")).thenReturn(Optional.empty());assertTrue(service.loadUserByUsername("ana").getAuthorities().isEmpty());}
 @Test void falhaParaLoginInexistente(){when(usuarioRepository.findByLogin("x")).thenReturn(Optional.empty());assertThrows(UsernameNotFoundException.class,()->service.loadUserByUsername("x"));verifyNoInteractions(colaboradorRepository);}
}
