package br.com.report_generator.usecase;

import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.service.api.RelatorioService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeletaRelatorioUseCase {

    private final RelatorioService relatorioService;

    public DeletaRelatorioUseCase(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    public UUID executar(UUID idRelatorio) {

        if (!this.relatorioService.existeRegistroParaId(idRelatorio)) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id " + idRelatorio);
        return this.relatorioService.deletarPorId(idRelatorio);
    }
}
