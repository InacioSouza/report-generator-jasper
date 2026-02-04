package br.com.report_generator.usecase.admin;

import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.ApiKey;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.utils.ApiKeyGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CadastraNovaChaveSistemaUseCaseAdmin {

    private final ApiKeyService apiKeyService;
    private final SistemaService sistemaService;

    public CadastraNovaChaveSistemaUseCaseAdmin(
            ApiKeyService apiKeyService,
            SistemaService sistemaService
    ) {
        this.apiKeyService = apiKeyService;
        this.sistemaService = sistemaService;
    }

    public String executar(UUID idSistema) {

        if(!this.sistemaService.existeRegistroParaId(idSistema)) throw new RegistroNaoEncontradoException(
                "Não foi encontrado sistema para o id: " + idSistema);

        List<ApiKey> listApiKeySistema = this.apiKeyService
                .buscaChavesPorIdSistema(idSistema);

        listApiKeySistema.forEach(apiKey -> {
            apiKey.setAtiva(Boolean.FALSE);
        });

        ApiKey novaApiKey = new ApiKey();
        novaApiKey.setSistema(this.sistemaService.findById(idSistema));

        novaApiKey.setCriadaEm(LocalDateTime.now());
        novaApiKey.setAtiva(Boolean.TRUE);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String novaChave = ApiKeyGenerator.gerar();
        novaApiKey.setHash(passwordEncoder.encode(novaChave));

        this.apiKeyService.save(novaApiKey);
        this.apiKeyService.saveAll(listApiKeySistema);

        return novaChave;
    }
}
