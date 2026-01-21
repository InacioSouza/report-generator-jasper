package br.com.report_generator.repository;

import br.com.report_generator.model.Relatorio;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RelatorioRepository extends GenericRepository<Relatorio, UUID>,
        JpaSpecificationExecutor<Relatorio> {

    @Query(
            value = """
                    SELECT COUNT(DISTINCT v.id) 
                    FROM relatorio r 
                    JOIN versao_relatorio v ON v.relatorio_id = r.id
                    WHERE r.id = :id
                    """,
            nativeQuery = true
    )
    Integer qtdVersoesParaORelatorio(UUID id);
}