package br.com.report_generator.repository;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiKeyRepository extends GenericRepository<ApiKey, Long> {

    @Query(value = """
                   SELECT a
                   FROM ApiKey a
                   WHERE a.sistema.id = :sistemaId
                   """
    )
    List<ApiKey> buscaChavesPorIdSistema(UUID sistemaId);
}
