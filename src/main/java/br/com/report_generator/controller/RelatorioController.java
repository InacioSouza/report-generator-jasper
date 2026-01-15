package br.com.report_generator.controller;

import br.com.report_generator.dto.relatorio.BaixarRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.service.api.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<RelatorioCadastradoResponseDto> cadastraRelatorio(
            @RequestPart("file")
            @NotNull
            MultipartFile file,
            @RequestPart("infos")
            @Valid
            CadastraRelatorioRequestDto infos) {
        return ResponseEntity.ok(this.relatorioService.uploadRelatorio(file, infos));
    }

    @PostMapping("/download")
    public void baixarRelatorio(@RequestBody BaixarRelatorioRequestDto dto,
                                HttpServletResponse response) {
        this.relatorioService.baixarRelatorio(dto, response);
    }

}
