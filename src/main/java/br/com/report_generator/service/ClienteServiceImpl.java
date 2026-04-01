package br.com.report_generator.service;

import br.com.report_generator.model.Cliente;
import br.com.report_generator.repository.ClienteRepository;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("br.com.report_generator.service.ClienteServiceImpl")
public class ClienteServiceImpl extends GenericServiceImpl<Cliente, UUID> implements ClienteService {

    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
