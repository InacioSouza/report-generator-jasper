package br.com.report_generator.service;

import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("br.com.report_generator.service.VersaoRelatorioServiceImpl")
public class VersaoRelatorioServiceImpl extends GenericServiceImpl<VersaoRelatorio, UUID> implements VersaoRelatorioService {

    private final VersaoRelatorioRepository repository;

    public VersaoRelatorioServiceImpl(VersaoRelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
