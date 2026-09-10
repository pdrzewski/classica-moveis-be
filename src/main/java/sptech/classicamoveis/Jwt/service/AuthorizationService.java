package sptech.classicamoveis.Jwt.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Usuario.model.Usuario;
import sptech.classicamoveis.Usuario.repository.UsuarioRepository;

@Service
public class AuthorizationService {

    private final UsuarioRepository usuarioRepository;

    public AuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica se o usuário autenticado tem a role especificada
     */
    public boolean temRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + role));
    }

    /**
     * Verifica se o usuário autenticado é ADMIN
     */
    public boolean isAdmin() {
        return temRole("ADMIN");
    }

    /**
     * Verifica se o usuário autenticado pode acessar o usuário com esse ID
     * Um usuário comum só pode acessar seus próprios dados
     * Um ADMIN pode acessar qualquer um
     */
    public boolean podeAcessarUsuario(Integer usuarioId) {
        if (isAdmin()) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        String username = auth.getName();
        Usuario usuarioAutenticado = usuarioRepository.findByLogin(username)
                .orElse(null);

        if (usuarioAutenticado == null) {
            return false;
        }

        return usuarioAutenticado.getId().equals(usuarioId);
    }

    /**
     * Obtém o ID do usuário autenticado
     */
    public Integer getIdUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String username = auth.getName();
        return usuarioRepository.findByLogin(username)
                .map(Usuario::getId)
                .orElse(null);
    }
}
