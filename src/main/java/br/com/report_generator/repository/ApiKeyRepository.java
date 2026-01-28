package br.com.report_generator.repository;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends GenericRepository<ApiKey, Long> {

    ApiKey findByHash(String hash);
}
