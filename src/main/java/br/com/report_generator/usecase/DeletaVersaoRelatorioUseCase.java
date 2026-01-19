package br.com.report_generator.usecase;

import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeletaVersaoRelatorioUseCase {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;

    public DeletaVersaoRelatorioUseCase(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService
    ) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
    }
    public UUID executar (UUID id) {

        if (!this.versaoRelatorioService.existeRegistroParaId(id)) throw new RegistroNaoEncontradoException(
                "Não foi encontrado versão de relatório para o id: " + id
        );

        VersaoRelatorio versaoRelatorio = this.versaoRelatorioService.findById(id);

        Relatorio relatorio = versaoRelatorio.getRelatorio();

        Integer qtdVersoesRelatorio = this.relatorioService.
                qtdVersoesParaORelatorio(relatorio.getId());

        this.versaoRelatorioService.delete(versaoRelatorio);

        if (qtdVersoesRelatorio > 1) {
            // Correspondia a penúltima versão antes de apagar o registro
            VersaoRelatorio ultimaVersao = this.versaoRelatorioService
                    .buscaVersaoRelatorioMaisRecentePara(relatorio.getId());

            relatorio.setNumeroUltimaVersao(ultimaVersao.getNumeroVersao());
        } else {
            relatorio.setNumeroUltimaVersao(-1);
        }

        this.relatorioService.update(relatorio);

        return versaoRelatorio.getId();
    }
}
