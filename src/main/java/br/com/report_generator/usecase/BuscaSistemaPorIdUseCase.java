package br.com.report_generator.usecase;

import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.utils.SecurityUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BuscaSistemaPorIdUseCase {

    private final SistemaService sistemaService;

    public BuscaSistemaPorIdUseCase(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    public SistemaResponseDto executar(UUID idSistema) {

        UUID idSistemaAutenticado = SecurityUtil.buscaIdSistemaAutenticado();

        if (!idSistema.equals(idSistemaAutenticado)) throw new IllegalArgumentException(
                "Você não pode alterar um registro sem ser o dono dele!");

        return new SistemaResponseDto(this.sistemaService.findById(idSistema));
    }
}
