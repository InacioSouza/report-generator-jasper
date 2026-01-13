package br.com.report_generator.service;

import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.repository.ArquivoSubreportRepository;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("br.com.report_generator.service.ArquivoSubreportServiceImpl")
public class ArquivoSubreportServiceImpl extends GenericServiceImpl<ArquivoSubreport, Long> implements ArquivoSubreportService {

    private final ArquivoSubreportRepository repository;

    public ArquivoSubreportServiceImpl(ArquivoSubreportRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<ArquivoSubreport> buscarSubreportsPorVersao(UUID versao) {
        return this.repository.buscarSubreportsPorVersao(versao);
    }
}