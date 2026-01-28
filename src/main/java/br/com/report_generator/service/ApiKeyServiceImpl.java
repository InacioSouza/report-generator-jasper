package br.com.report_generator.service;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.repository.ApiKeyRepository;
import br.com.report_generator.service.api.ApiKeyService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service("br.com.report_generator.service.ApiKeyServiceImpl")
public class ApiKeyServiceImpl extends GenericServiceImpl<ApiKey, Long> implements ApiKeyService {

    private final ApiKeyRepository repository;

    public ApiKeyServiceImpl(ApiKeyRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
