package sptech.classicamoveis.Produto.exception;

import java.time.Instant;
import java.util.Map;

public record ProdutoErroResponse(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
