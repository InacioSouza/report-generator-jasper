package br.com.report_generator.service;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.repository.SistemaRepository;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("br.com.report_generator.service.SistemaServiceImpl")
public class SistemaServiceImpl extends GenericServiceImpl<Sistema, UUID> implements SistemaService {

    private final SistemaRepository repository;

    SistemaServiceImpl(SistemaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public SistemaResponseDto atualiza(UUID id, SistemaRequestDto dto) {

        if (!this.repository.existsById(id)) throw new RegistroNaoEncontradoException(
                "Não foi encontrado sistema para o id " + id);

        Sistema sistemaEncontrado = this.repository.findById(id).get();

        sistemaEncontrado.setNome(dto.nome());
        sistemaEncontrado.setDescricao(dto.descricao());

        this.update(sistemaEncontrado);
        return new SistemaResponseDto(sistemaEncontrado);
    }
}
