package br.com.report_generator.controller;

import br.com.report_generator.dto.filtros.RelatorioFiltroDto;
import br.com.report_generator.dto.relatorio.*;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.AtualizaRelatorioUseCase;
import br.com.report_generator.usecase.BaixarTemplateRelatorioUseCase;
import br.com.report_generator.usecase.CadastrarRelatorioUseCase;
import br.com.report_generator.usecase.DeletaRelatorioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api-r/relatorio")
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

        return ResponseEntity.status(HttpStatus.CREATED).body(
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

    @PostMapping("/informacoes-relatorio")
    public ResponseEntity<List<InfoRelatorioResponseDto>> buscaInformacoesDosRelatorios(
            @RequestBody RelatorioFiltroDto filtro) {
        return ResponseEntity.ok(this.relatorioService.buscaRelatorios(filtro));
    }

    @PutMapping
    public ResponseEntity<InfoRelatorioResponseDto> atualizarRelatorio(
            @RequestBody AtualizaRelatorioRequestDto dto) {
        return ResponseEntity.ok(
                new AtualizaRelatorioUseCase(sistemaService, relatorioService).executar(dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deletaRelatorio(@PathVariable UUID id) {
        return ResponseEntity.ok(new DeletaRelatorioUseCase(this.relatorioService).executar(id));
    }
}
