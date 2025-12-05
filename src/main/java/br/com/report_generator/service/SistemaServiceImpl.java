package br.com.report_generator.service;

import br.com.report_generator.model.Sistema;
import br.com.report_generator.repository.SistemaRepository;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service("br.com.report_generator.service.SistemaServiceImpl")
public class SistemaServiceImpl extends GenericServiceImpl<Sistema, Long> implements SistemaService {

    private final SistemaRepository repository;

    SistemaServiceImpl(SistemaRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
