package br.com.report_generator.service.api;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService extends GenericService<ApiKey, Long> {

    List<ApiKey> buscaChavesPorIdSistema(UUID idSistema);
}
