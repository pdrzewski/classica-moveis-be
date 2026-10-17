package sptech.classicamoveis.Representante;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Representante.controller.RepresentanteController;
import sptech.classicamoveis.Representante.dto.FornecedorRepresentantesResponseDTO;
import sptech.classicamoveis.Representante.dto.RepresentanteRequestDTO;
import sptech.classicamoveis.Representante.dto.RepresentanteResponseDTO;
import sptech.classicamoveis.Representante.service.RepresentanteService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepresentanteControllerTest {

    @Mock RepresentanteService service;
    @InjectMocks RepresentanteController controller;

    @Test
    void listarComTermo() {
        when(service.listar("ana")).thenReturn(List.of(mock(RepresentanteResponseDTO.class)));

        ResponseEntity<List<RepresentanteResponseDTO>> r = controller.listar("ana");

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().size());
    }

    @Test
    void buscarPorId() {
        RepresentanteResponseDTO resp = mock(RepresentanteResponseDTO.class);
        when(service.buscarPorId(1L)).thenReturn(resp);

        assertSame(resp, controller.buscarPorId(1L).getBody());
    }

    @Test
    void listarPorFornecedor() {
        FornecedorRepresentantesResponseDTO resp = mock(FornecedorRepresentantesResponseDTO.class);
        when(service.listarPorFornecedor(2L)).thenReturn(resp);

        assertSame(resp, controller.listarPorFornecedor(2L).getBody());
    }

    @Test
    void criarRetorna201ComLocation() {
        RepresentanteRequestDTO req = mock(RepresentanteRequestDTO.class);
        RepresentanteResponseDTO criado = mock(RepresentanteResponseDTO.class);
        when(criado.id()).thenReturn(7L);
        when(service.criar(req)).thenReturn(criado);

        ResponseEntity<RepresentanteResponseDTO> r = controller.criar(req);

        assertEquals(HttpStatus.CREATED, r.getStatusCode());
        assertEquals("/representantes/7", r.getHeaders().getLocation().toString());
        assertSame(criado, r.getBody());
    }

    @Test
    void atualizar() {
        RepresentanteRequestDTO req = mock(RepresentanteRequestDTO.class);
        RepresentanteResponseDTO resp = mock(RepresentanteResponseDTO.class);
        when(service.atualizar(1L, req)).thenReturn(resp);

        assertSame(resp, controller.atualizar(1L, req).getBody());
    }

    @Test
    void deletarRetorna204() {
        ResponseEntity<Void> r = controller.deletar(1L);

        assertEquals(HttpStatus.NO_CONTENT, r.getStatusCode());
        verify(service).deletar(1L);
    }
}
