package sptech.classicamoveis.Relatorio;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;

@RestController
@RequestMapping("/relatorios")
@Tag(
        name = "Relatórios",
        description = "Relatórios de vendas"
)
public class RelatorioController {

    private final RelatorioService relatorioService;

    private final RelatorioPdfGenerator relatorioPdfGenerator;

    public RelatorioController(
            RelatorioService relatorioService,
            RelatorioPdfGenerator relatorioPdfGenerator) {

        this.relatorioService = relatorioService;
        this.relatorioPdfGenerator = relatorioPdfGenerator;
    }

    @GetMapping("/venda/{movimentacaoId}")
    public ResponseEntity<RelatorioVendaDto> gerarRelatorioVenda(
            @PathVariable Integer movimentacaoId) {

        return ResponseEntity.ok(
                relatorioService.gerarRelatorioVenda(
                        movimentacaoId
                )
        );
    }

    @GetMapping("/venda/{movimentacaoId}/pdf")
    public ResponseEntity<byte[]> gerarRelatorioVendaPdf(
            @PathVariable Integer movimentacaoId) {

        RelatorioVendaDto relatorio =
                relatorioService.gerarRelatorioVenda(
                        movimentacaoId
                );

        byte[] pdf =
                relatorioPdfGenerator.gerarPdf(
                        relatorio
                );

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_PDF
        );

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(
                                "pedido-"
                                        + movimentacaoId
                                        + ".pdf"
                        )
                        .build()
        );

        headers.setContentLength(
                pdf.length
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
}