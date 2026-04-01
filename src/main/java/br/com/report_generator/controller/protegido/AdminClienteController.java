package br.com.report_generator.controller.protegido;

import br.com.report_generator.dto.*;
import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.usecase.admin.CadastraClienteUseCaseAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointPrefix.ADMIN + "/cliente")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Administrador")
public class AdminClienteController {

    private final ClienteService clienteService;
    private final ApiKeyService apiKeyService;

    public AdminClienteController(
            ClienteService clienteService,
            ApiKeyService apiKeyService) {
        this.clienteService = clienteService;
        this.apiKeyService = apiKeyService;
    }

    @Operation(
            summary = "Realiza o cadastro de clientes (Equivalente a um usuário da aplicação)"
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteCadastradoResponseDto> cadastra(
            @RequestBody ClienteRequestDto clienteRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    new CadastraClienteUseCaseAdmin(
                            this.clienteService,
                            this.apiKeyService
                    ).executar(clienteRequest)
                );
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ClienteResponseDto>> buscaTodos() {
        return ResponseEntity.ok(
                this.clienteService
                        .findAll()
                        .stream()
                        .map(ClienteResponseDto::new)
                        .toList()
        );
    }
}
