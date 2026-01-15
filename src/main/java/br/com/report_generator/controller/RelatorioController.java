package br.com.report_generator.controller;

import br.com.report_generator.dto.*;
import br.com.report_generator.service.api.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(
            summary = "End-point para realizar o cadastro de relatórios",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                    )
            )
    )
    @PostMapping(
            value = "/cadastrar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> cadastraRelatorio(
            @RequestPart("file")
            @NotNull
            MultipartFile file,
            @RequestPart("infos")
            @Valid
            CadastraRelatorioDto infos) {
        return ResponseEntity.ok(this.relatorioService.uploadRelatorio(file, infos));
    }

    @PostMapping("/download")
    public void baixarRelatorio(@RequestBody BaixarRelatorioRequestDto dto,
                                HttpServletResponse response) {
        this.relatorioService.baixarRelatorio(dto, response);
    }

    @PostMapping(
            value = "/pedido",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> gerarRelatorio(@RequestBody PedidoRelatorioDTO pedidoDTO) {

        PdfGeradoDto pdfGerado = this.relatorioService.gerarRelatorio(pedidoDTO);
        String headers = pedidoDTO.exibicaoRelatorio() + pdfGerado.nome();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGerado.pdf());
    }
}
