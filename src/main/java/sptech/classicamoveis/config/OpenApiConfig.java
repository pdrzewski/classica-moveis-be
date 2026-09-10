package sptech.classicamoveis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração central do Swagger / OpenAPI (springdoc-openapi).
 *
 * Documentação interativa disponível em:
 *   - Swagger UI:  http://localhost:8080/swagger-ui.html
 *   - JSON da API: http://localhost:8080/v3/api-docs
 *
 * Essas duas rotas já estão liberadas em SecurityConfiguracao (URLS_PERMITIDAS),
 * então não é necessário estar autenticado para visualizar a documentação.
 *
 * Para chamar endpoints protegidos direto pelo Swagger UI:
 *   1. Faça POST em /login com { "login": "...", "senha": "..." } e copie o token retornado.
 *   2. Clique no botão "Authorize" (cadeado) no topo da página do Swagger.
 *   3. Cole o token (sem o prefixo "Bearer ") no campo "bearerAuth" e confirme.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Classica Móveis API",
                version = "v1",
                description = """
                        API REST do sistema de gestão da Classica Móveis: cadastro de produtos, \
                        categorias, fornecedores, clientes, colaboradores, estabelecimentos, \
                        controle de estoque/movimentações (vendas, compras, transferências) e relatórios.

                        Autenticação via JWT: gere o token em POST /login e envie-o no header \
                        Authorization: Bearer {token} nas demais requisições.""",
                contact = @Contact(name = "Equipe Classica Móveis"),
                license = @License(name = "Uso interno")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente local")
        },
        security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Informe o token JWT obtido em POST /login"
)
public class OpenApiConfig {
}
