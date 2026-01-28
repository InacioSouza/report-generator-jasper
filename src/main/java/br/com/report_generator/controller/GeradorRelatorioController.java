package br.com.report_generator.controller;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDto;
import br.com.report_generator.service.api.GeradorRelatorioService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.usecase.GeraRelatorioUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gerador-relatorio")
public class GeradorRelatorioController {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;
    private final GeradorRelatorioService geradorRelatorioService;

    public GeradorRelatorioController(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService,
            GeradorRelatorioService geradorRelatorioService) {

        this.geradorRelatorioService = geradorRelatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
        this.relatorioService = relatorioService;
    }

    @PostMapping(
            value = "/pedido",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> gerarRelatorio(@RequestBody GeraRelatorioRequestDto pedidoDTO) {

        PdfGeradoDto pdfGerado = new GeraRelatorioUseCase(
                this.relatorioService,
                this.versaoRelatorioService,
                this.geradorRelatorioService
        ).executar(pedidoDTO);

        String headers = pedidoDTO.exibicaoRelatorio() + "; filename=\"" + pdfGerado.nome() + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGerado.pdf());
    }
}
