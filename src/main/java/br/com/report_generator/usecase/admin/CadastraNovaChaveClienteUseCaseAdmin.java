package br.com.report_generator.usecase.admin;

import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.ApiKey;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.service.utils.ApiKeyGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CadastraNovaChaveClienteUseCaseAdmin {

    private final ApiKeyService apiKeyService;
    private final ClienteService clienteService;

    public CadastraNovaChaveClienteUseCaseAdmin(
            ApiKeyService apiKeyService,
            ClienteService clienteService
    ) {
        this.apiKeyService = apiKeyService;
        this.clienteService = clienteService;
    }

    public String executar(UUID idCliente) {

        if(!this.clienteService.existeRegistroParaId(idCliente)) {
            throw new RegistroNaoEncontradoException(
                    "Não foi encontrado cliente para o id: " + idCliente);
        }

        List<ApiKey> listApiKeySistema = this.apiKeyService
                .buscaChavesPorIdCliente(idCliente);

        listApiKeySistema.forEach(apiKey -> {
            apiKey.setAtiva(Boolean.FALSE);
        });

        ApiKey novaApiKey = new ApiKey();
        novaApiKey.setCliente(this.clienteService.findById(idCliente));

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
