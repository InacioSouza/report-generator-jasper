package br.com.report_generator.controller;

import br.com.report_generator.dto.filtros.VersaoRelatorioFiltroDto;
import br.com.report_generator.dto.versaorelatorio.AtualizaVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioResponseDto;
import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.usecase.BuscaVersaoRelatorioComFiltroUseCase;
import br.com.report_generator.usecase.DeletaVersaoRelatorioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(EndpointPrefix.API + "/versao-relatorio")
@SecurityRequirement(name = "apiKeyAuth")
@SecurityRequirement(name = "clientIdAuth")
public class VersaoRelatorioController {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;

    public VersaoRelatorioController(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService
    ) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
    }

    @PostMapping(
            path = "/nova-versao",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> cadastrarNovaVersaoRelatorio(@RequestPart("versaoZip") MultipartFile versaoZip,
                                                          @RequestPart("infos")
                                                          @Valid
                                                          CadastraVersaoRelatorioRequestDto infos) {

        Relatorio relatorio = this.relatorioService.findById(infos.idRelatorio());
        if (relatorio == null) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id: " + infos.idRelatorio()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        this.versaoRelatorioService
                                .cadastraVersaoRelatorio(versaoZip, infos, relatorio)
                );
    }

    @Operation(summary = "Busca informações de versões de relatório de acordo com os filtros passados")
    @PostMapping("/filtro")
    public ResponseEntity<List<VersaoRelatorioResponseDto>> buscarVersaoRelatorio(VersaoRelatorioFiltroDto filtroDto) {

        return ResponseEntity.ok(new BuscaVersaoRelatorioComFiltroUseCase(
                (VersaoRelatorioRepository) this.versaoRelatorioService.getRepository()
        ).executar(filtroDto));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<UUID> deletarVersaoRelatorio(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new DeletaVersaoRelatorioUseCase(
                        this.relatorioService,
                        this.versaoRelatorioService
                ).executar(id));
    }

    @PutMapping
    public ResponseEntity<?> atualizarVersaoRelatorio(AtualizaVersaoRelatorioRequestDto dto) {
        return ResponseEntity.ok(this.versaoRelatorioService.atualizar(dto));
    }
}
