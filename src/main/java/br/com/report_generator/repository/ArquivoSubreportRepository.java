package br.com.report_generator.repository;

import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArquivoSubreportRepository extends GenericRepository<ArquivoSubreport, Long> {

    @Query(
            value = """
                    SELECT DISTINCT S FROM ArquivoSubreport S WHERE S.versaoRelatorio.id = :versao
                    """
    )
    List<ArquivoSubreport> buscarSubreportsPorVersao(UUID versao);
}
