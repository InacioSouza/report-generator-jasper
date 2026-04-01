package br.com.report_generator.usecase.admin;

import br.com.report_generator.dto.ClienteCadastradoResponseDto;
import br.com.report_generator.dto.ClienteRequestDto;
import br.com.report_generator.model.ApiKey;
import br.com.report_generator.model.Cliente;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.service.utils.ApiKeyGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CadastraClienteUseCaseAdmin {

    private final ClienteService clienteService;
    private final ApiKeyService apiKeyService;

    public CadastraClienteUseCaseAdmin(
            ClienteService clienteService,
            ApiKeyService apiKeyService
    ) {
        this.clienteService = clienteService;
        this.apiKeyService = apiKeyService;
    }

    public ClienteCadastradoResponseDto executar(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente = this.clienteService.save(cliente);

        ApiKey apiKey = new ApiKey();
        apiKey.setCliente(cliente);
        apiKey.setAtiva(true);
        String apiKeyGerada = ApiKeyGenerator.gerar(cliente.getId());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        apiKey.setHash(
                passwordEncoder.encode(apiKeyGerada)
        );
        apiKey.setCriadaEm(LocalDateTime.now());
        this.apiKeyService.save(apiKey);

        return new ClienteCadastradoResponseDto(
                cliente.getId(),
                apiKeyGerada
        );
    }
}
