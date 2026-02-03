package br.com.report_generator.controller.protegido;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.CadastraSistemaUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/cadastra-sistema")
public class CadastraSistemaController {

    private final SistemaService sistemaService;
    private final ApiKeyService apiKeyService;

    public CadastraSistemaController(
            SistemaService sistemaService,
            ApiKeyService apiKeyService) {
        this.sistemaService = sistemaService;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> cadastra(@RequestBody SistemaRequestDto sistemaRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    new CadastraSistemaUseCase(
                            this.sistemaService,
                            this.apiKeyService
                    ).executar(sistemaRequest)
                );
    }
}
