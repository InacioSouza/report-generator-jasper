package br.com.report_generator.controller.protegido;

import br.com.report_generator.dto.SistemaCadastradoResponseDto;
import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.CadastraSistemaUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cadastra-sistema")
public class AdminSistemaController {

    private final SistemaService sistemaService;
    private final ApiKeyService apiKeyService;

    public AdminSistemaController(
            SistemaService sistemaService,
            ApiKeyService apiKeyService) {
        this.sistemaService = sistemaService;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<SistemaCadastradoResponseDto> cadastra(@RequestBody SistemaRequestDto sistemaRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    new CadastraSistemaUseCase(
                            this.sistemaService,
                            this.apiKeyService
                    ).executar(sistemaRequest)
                );
    }

    @GetMapping
    public ResponseEntity<List<SistemaResponseDto>> buscaTodos() {
        return ResponseEntity.ok(this.sistemaService.findAll().stream().map(SistemaResponseDto::new).toList());
    }
}
