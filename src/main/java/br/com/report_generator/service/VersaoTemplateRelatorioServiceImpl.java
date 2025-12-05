package br.com.report_generator.service;

import br.com.report_generator.model.VersaoTemplateRelatorio;
import br.com.report_generator.repository.VersaoTemplateRelatorioRepository;
import br.com.report_generator.service.api.VersaoTemplateRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;

import java.util.UUID;

public class VersaoTemplateRelatorioServiceImpl extends GenericServiceImpl<VersaoTemplateRelatorio, UUID> implements VersaoTemplateRelatorioService {

    private final VersaoTemplateRelatorioRepository repository;

    public VersaoTemplateRelatorioServiceImpl(VersaoTemplateRelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
