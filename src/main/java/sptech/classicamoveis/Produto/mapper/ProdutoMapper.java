package sptech.classicamoveis.Produto.mapper;

import org.springframework.stereotype.Component;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        if (produto == null) {
            return null;
        }
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getFornecedor().getId(),
                produto.getCategoria().getId(),
                produto.getNome(),
                produto.getSku(),
                produto.getCodigoBarras(),
                produto.getUnidadeMedida(),
                produto.getMarca(),
                produto.getPrecoCusto(),
                produto.getPrecoVenda(),
                produto.getEstoqueMinimo(),
                produto.getAtivo()
        );
    }

    public Produto toEntity(ProdutoRequestDTO dto, Fornecedor fornecedor, Categoria categoria) {
        if (dto == null) {
            return null;
        }
        Produto produto = new Produto();
        preencherEntidade(produto, dto, fornecedor, categoria);
        return produto;
    }

    public void preencherEntidade(Produto produto, ProdutoRequestDTO dto, Fornecedor fornecedor, Categoria categoria) {
        produto.setNome(dto.nome());
        produto.setFornecedor(fornecedor);
        produto.setCategoria(categoria);
        produto.setSku(dto.sku());
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setMarca(dto.marca());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setAtivo(dto.ativo());
    }
}
