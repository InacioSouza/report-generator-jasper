package br.com.report_generator.controller;

import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.dto.relatorio.BaixarRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.InfoRelatorioResponseDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.BaixarTemplateRelatorioUseCase;
import br.com.report_generator.usecase.CadastrarRelatorioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;
    private final ArquivoSubreportService arquivoSubreportService;
    private final SistemaService sistemaService;

    public RelatorioController(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService,
            ArquivoSubreportService arquivoSubreportService,
            SistemaService sistemaService
    ) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
        this.arquivoSubreportService = arquivoSubreportService;
        this.sistemaService = sistemaService;
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

        return ResponseEntity.ok(
                new CadastrarRelatorioUseCase(sistemaService, relatorioService)
                        .executar(file, infos)
        );
    }

    @PostMapping("/download")
    public void baixarRelatorio(@RequestBody BaixarRelatorioRequestDto dto,
                                HttpServletResponse response) {

        new BaixarTemplateRelatorioUseCase(
                this.relatorioService,
                this.versaoRelatorioService,
                this.arquivoSubreportService
        ).executar(dto, response);
    }

    @GetMapping("/informacao-completa")
    public ResponseEntity<List<InfoRelatorioResponseDto>> buscaInformacoesDeTodosRelatorios() {
        return ResponseEntity.ok(this.relatorioService.buscaInformacaoDeTodosRelatorios());
    }

}
