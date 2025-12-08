package br.com.report_generator.service;

import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.repository.ArquivoSubreportRepository;
import br.com.report_generator.service.generic.GenericServiceImpl;

public class ArquivoSubreportServiceImpl extends GenericServiceImpl<ArquivoSubreport, Long> {

    private final ArquivoSubreportRepository repository;

    public ArquivoSubreportServiceImpl(ArquivoSubreportRepository repository) {
        super(repository);
        this.repository = repository;
    }
}