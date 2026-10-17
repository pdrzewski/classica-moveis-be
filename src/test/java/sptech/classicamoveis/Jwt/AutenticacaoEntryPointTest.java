package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import sptech.classicamoveis.Jwt.config.AutenticacaoEntryPoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutenticacaoEntryPointTest {

    @Test
    void respondeComUnauthorizedEMensagem() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        new AutenticacaoEntryPoint().commence(new MockHttpServletRequest(), response, new BadCredentialsException("x"));

        assertEquals(401, response.getStatus());
        assertTrue(response.getErrorMessage().contains("token JWT"));
    }
}
