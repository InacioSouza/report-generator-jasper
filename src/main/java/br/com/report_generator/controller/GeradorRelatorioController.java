package br.com.report_generator.controller;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDTO;
import br.com.report_generator.service.api.GeradorRelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gerador-relatorio")
public class GeradorRelatorioController {

    private final GeradorRelatorioService geradorRelatorioService;

    public GeradorRelatorioController(GeradorRelatorioService geradorRelatorioService) {
        this.geradorRelatorioService = geradorRelatorioService;
    }

    @PostMapping(
            value = "/pedido",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> gerarRelatorio(@RequestBody GeraRelatorioRequestDTO pedidoDTO) {

        PdfGeradoDto pdfGerado = this.geradorRelatorioService.gerarRelatorio(pedidoDTO);
        String headers = pedidoDTO.exibicaoRelatorio() + pdfGerado.nome();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGerado.pdf());
    }
}
