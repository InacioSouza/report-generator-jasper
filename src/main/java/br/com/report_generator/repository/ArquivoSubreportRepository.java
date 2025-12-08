package br.com.report_generator.repository;

import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoSubreportRepository extends GenericRepository<ArquivoSubreport, Long> {
}
