package br.com.report_generator.service;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.repository.ApiKeyRepository;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("br.com.report_generator.service.ApiKeyServiceImpl")
public class ApiKeyServiceImpl extends GenericServiceImpl<ApiKey, Long> implements ApiKeyService {

    private final ApiKeyRepository repository;

    public ApiKeyServiceImpl(ApiKeyRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<ApiKey> buscaChavesPorIdSistema(UUID idSistema) {
        return this.repository.buscaChavesPorIdSistema(idSistema);
    }
}
