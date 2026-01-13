package br.com.report_generator.controller;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.dto.PdfGerado;
import br.com.report_generator.dto.PedidoRelatorioDTO;
import br.com.report_generator.service.api.RelatorioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @PostMapping
    public ResponseEntity<?> cadastraRelatorio(
            @RequestPart("relatorioZip")
            @NotNull
            MultipartFile relatorioZip,
            @RequestPart("infos")
            @Valid
            CadastraRelatorioDto infos) {
        return ResponseEntity.ok(this.relatorioService.uploadRelatorio(relatorioZip, infos));
    }

    @PostMapping(value = "/pedido", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarRelatorio(@RequestBody PedidoRelatorioDTO pedidoDTO) {

        PdfGerado pdfGerado = this.relatorioService.gerarRelatorio(pedidoDTO);
        String headers = pedidoDTO.exibicaoRelatorio() + pdfGerado.nome();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGerado.pdf());
    }
}
