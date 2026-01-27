package br.com.report_generator.usecase;

import br.com.report_generator.dto.relatorio.AtualizaRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.InfoRelatorioResponseDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.stereotype.Component;

@Component
public class AtualizaRelatorioUseCase {

    private final SistemaService sistemaService;
    private final RelatorioService relatorioService;

    public AtualizaRelatorioUseCase(
            SistemaService sistemaService,
            RelatorioService relatorioService
    ) {
        this.sistemaService = sistemaService;
        this.relatorioService = relatorioService;
    }

    public InfoRelatorioResponseDto executar(AtualizaRelatorioRequestDto dto) {

        Sistema sistema = null;
        if (dto.idSistema() != null) {
            sistema = this.sistemaService.findById(dto.idSistema());
        }

        return this.relatorioService.atualizarRelatorio(dto, sistema);
    }

}
