package br.com.report_generator.usecase.sistema;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AtualizaSistemaUseCase {

    private final SistemaService sistemaService;

    public AtualizaSistemaUseCase(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    public SistemaResponseDto executar(UUID id, SistemaRequestDto dto) {
        return this.sistemaService.atualiza(id, dto);
    }
}
