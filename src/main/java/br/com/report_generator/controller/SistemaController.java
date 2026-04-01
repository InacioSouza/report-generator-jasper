package br.com.report_generator.controller;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.sistema.AtualizaSistemaUseCase;
import br.com.report_generator.usecase.sistema.BuscaSistemaPorIdUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(EndpointPrefix.API + "/sistema")
@SecurityRequirement(name = "apiKeyAuth")
@SecurityRequirement(name = "clientIdAuth")
public class SistemaController {

    private final SistemaService service;

    public SistemaController(SistemaService sistemaService) {
        this.service = sistemaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SistemaResponseDto> buscaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(new BuscaSistemaPorIdUseCase(this.service).executar(id));
    }

    @PostMapping("/")
    public ResponseEntity<SistemaResponseDto> cadastrar(
            @RequestBody SistemaRequestDto dto) {
        return ResponseEntity.ok(
                new SistemaResponseDto(
                    this.service.save(
                            new Sistema(dto.nome(), dto.descricao())
                    )
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SistemaResponseDto> atualiza(
            @PathVariable UUID id,
            @RequestBody SistemaRequestDto dto
    ) {
        return ResponseEntity.ok(new AtualizaSistemaUseCase(this.service).executar(id, dto));
    }
}
