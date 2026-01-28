package br.com.report_generator.usecase;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.model.ApiKey;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.utils.ApiKeyGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CadastraSistemaUseCase {

    private final SistemaService sistemaService;
    private final ApiKeyService apiKeyService;

    public CadastraSistemaUseCase(
            SistemaService sistemaService,
            ApiKeyService apiKeyService
    ) {
        this.sistemaService = sistemaService;
        this.apiKeyService = apiKeyService;
    }

    public String executar(SistemaRequestDto dto) {
        Sistema sistema = new Sistema();
        sistema.setNome(dto.nome());
        sistema.setDescricao(dto.descricao());
        sistema = this.sistemaService.save(sistema);

        ApiKey apiKey = new ApiKey();
        apiKey.setSistema(sistema);
        apiKey.setAtiva(true);
        String apiKeyGerada = ApiKeyGenerator.gerar();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        apiKey.setHash(
                passwordEncoder.encode(apiKeyGerada)
        );
        apiKey.setCriadaEm(LocalDateTime.now());
        this.apiKeyService.save(apiKey);

        return apiKeyGerada;
    }
}
