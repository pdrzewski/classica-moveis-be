package sptech.classicamoveis.Mapper;

import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;
import sptech.classicamoveis.Produto.mapper.ProdutoMapper;
import sptech.classicamoveis.Produto.model.Produto;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoMapperTest {

    private final ProdutoMapper mapper = new ProdutoMapper();

    private Fornecedor fornecedor() {
        Fornecedor f = new Fornecedor();
        f.setNome("Fornecedor X");
        return f;
    }

    private Categoria categoria() {
        return new Categoria(1, "Sofás");
    }

    private ProdutoRequestDTO request() {
        return new ProdutoRequestDTO(1L, 1, "Sofá", "SKU1", "789", "9401", "5102", "UN", "Marca",
                500.0, 900.0, 3, true);
    }

    @Test
    void toResponseDtoNuloRetornaNulo() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toResponseDtoMapeiaCamposENomesDeRelacionamentos() {
        Produto p = mapper.toEntity(request(), fornecedor(), categoria());
        p.setId(10);

        ProdutoResponseDTO dto = mapper.toResponseDTO(p);

        assertEquals(10, dto.id());
        assertEquals("Fornecedor X", dto.fornecedor());
        assertEquals("Sofás", dto.categoria());
        assertEquals("Sofá", dto.nome());
        assertEquals("SKU1", dto.sku());
        assertEquals("789", dto.codigoBarras());
        assertEquals("9401", dto.ncm());
        assertEquals("5102", dto.cfop());
        assertEquals("UN", dto.unidadeMedida());
        assertEquals("Marca", dto.marca());
        assertEquals(500.0, dto.precoCusto());
        assertEquals(900.0, dto.precoVenda());
        assertEquals(3, dto.estoqueMinimo());
        assertTrue(dto.ativo());
    }

    @Test
    void toEntityNuloRetornaNulo() {
        assertNull(mapper.toEntity(null, fornecedor(), categoria()));
    }

    @Test
    void preencherEntidadeSobrescreveOsCampos() {
        Produto existente = new Produto();
        existente.setNome("antigo");
        Fornecedor f = fornecedor();
        Categoria c = categoria();

        mapper.preencherEntidade(existente, request(), f, c);

        assertEquals("Sofá", existente.getNome());
        assertSame(f, existente.getFornecedor());
        assertSame(c, existente.getCategoria());
        assertEquals(900.0, existente.getPrecoVenda());
        assertEquals(3, existente.getEstoqueMinimo());
        assertTrue(existente.getAtivo());
    }
}
