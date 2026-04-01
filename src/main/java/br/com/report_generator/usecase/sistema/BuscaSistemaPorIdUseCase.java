package br.com.report_generator.usecase.sistema;

import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BuscaSistemaPorIdUseCase {

    private final SistemaService sistemaService;

    public BuscaSistemaPorIdUseCase(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    public SistemaResponseDto executar(UUID idSistema) {
        return new SistemaResponseDto(this.sistemaService.findById(idSistema));
    }
}
