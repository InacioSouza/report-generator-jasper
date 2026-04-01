package br.com.report_generator.controller.protegido;

import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.usecase.admin.CadastraNovaChaveClienteUseCaseAdmin;
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
    private final ClienteService clienteService;

    public AdminApiKey(
            ApiKeyService apiKeyService,
            ClienteService clienteService
    ) {
        this.clienteService = clienteService;
        this.apiKeyService = apiKeyService;
    }

    @Operation(summary = "Cadastra uma nova chave para um cliente, desativa todas as outras chaves do cliente")
    @PostMapping("/{idCliente}")
    public ResponseEntity<?> cadastraNovaChaveParaCliente(@PathVariable UUID idCliente) {
        return ResponseEntity.ok(
                new CadastraNovaChaveClienteUseCaseAdmin(
                        this.apiKeyService,
                        this.clienteService
                ).executar(idCliente));
    }
}
