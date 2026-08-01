package sptech.classicamoveis.Jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Colaborador.Colaborador;
import sptech.classicamoveis.Colaborador.ColaboradorRepository;
import sptech.classicamoveis.Usuario.Usuario;
import sptech.classicamoveis.Usuario.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ColaboradorRepository colaboradorRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository, ColaboradorRepository colaboradorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou Senha inválidos"));

        Colaborador colaborador = colaboradorRepository.findByUsuario_Login(login).orElse(null);

        return new UsuarioAutenticado(usuario, colaborador);
    }
}