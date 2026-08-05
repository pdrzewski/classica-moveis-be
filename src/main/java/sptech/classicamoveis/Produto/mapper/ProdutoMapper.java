package sptech.classicamoveis.Produto.mapper;

import org.springframework.stereotype.Component;
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
                produto.getNome(),
                produto.getFornecedor(),
                produto.getCategoria(),
                produto.getPrecoCusto(),
                produto.getEstoqueMin(),
                produto.getNcm()
        );
    }

    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Produto produto = new Produto();
        preencherEntidade(produto, dto);
        return produto;
    }

    public void preencherEntidade(Produto produto, ProdutoRequestDTO dto) {
        produto.setNome(dto.nome());
        produto.setFornecedor(dto.fornecedor());
        produto.setCategoria(dto.categoria());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setEstoqueMin(dto.estoqueMin());
        produto.setNcm(dto.ncm());
    }

}