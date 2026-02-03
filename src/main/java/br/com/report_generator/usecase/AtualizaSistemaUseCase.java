package br.com.report_generator.usecase;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AtualizaSistemaUseCase {

    private final SistemaService sistemaService;

    public AtualizaSistemaUseCase(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    public SistemaResponseDto executar(UUID id, SistemaRequestDto dto) {

        UUID idSistemaConectado = (UUID) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!id.equals(idSistemaConectado)) throw new IllegalArgumentException(
                "Você não pode alterar um registro sem ser o dono dele!");

        return this.sistemaService.atualiza(id, dto);
    }
}
