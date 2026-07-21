package sptech.classicamoveis.Estoque;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Compra.Compra;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoComItensDto;
import sptech.classicamoveis.Produto.ProdutoService;
import sptech.classicamoveis.Transferencia.Transferencia;
import sptech.classicamoveis.Venda.Venda;


import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class EstoqueController {

    private final ProdutoService service;

    public EstoqueController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> historico() {
        return ResponseEntity.ok(service.listarTodasMovimentacoes());
    }

    @PostMapping("/venda")
    public ResponseEntity<Venda> novaVenda(@RequestBody MovimentacaoComItensDto<Venda> dto) {
        Venda vendaSalva = service.registrarVenda(dto.getDados(), dto.getItens());
        return ResponseEntity.ok(vendaSalva);
    }

    @PostMapping("/compra")
    public ResponseEntity<Compra> novaCompra(@RequestBody MovimentacaoComItensDto<Compra> dto) {
        Compra compraSalva = service.registrarCompra(dto.getDados(), dto.getItens());
        return ResponseEntity.ok(compraSalva);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Transferencia> novaTransferencia(@RequestBody MovimentacaoComItensDto<Transferencia> dto) {
        Transferencia transferenciaSalva = service.registrarTransferencia(dto.getDados(), dto.getItens());
        return ResponseEntity.ok(transferenciaSalva);
    }
}
