package br.com.report_generator.controller.protegido;

import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.usecase.admin.CadastraNovaChaveSistemaUseCaseAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(EndpointPrefix.ADMIN + "/chave-api")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Administrador")
public class AdminApiKey {

    private final ApiKeyService apiKeyService;
    private final SistemaService sistemaService;

    public AdminApiKey(
            ApiKeyService apiKeyService,
            SistemaService sistemaService
    ) {
        this.sistemaService = sistemaService;
        this.apiKeyService = apiKeyService;
    }

    @Operation(summary = "Cadastra uma nova chave para um sistema, desativa todas as outras")
    @PostMapping("/{idSistema}")
    public ResponseEntity<?> cadastraNovaChaveParaSistema(@PathVariable UUID idSistema) {
        return ResponseEntity.ok(new CadastraNovaChaveSistemaUseCaseAdmin(
                this.apiKeyService, this.sistemaService).executar(idSistema));
    }
}
