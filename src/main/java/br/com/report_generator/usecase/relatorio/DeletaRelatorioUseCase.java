package br.com.report_generator.usecase.relatorio;

import br.com.report_generator.infra.exception.FalhaAutenticacaoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.utils.SecurityUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeletaRelatorioUseCase {

    private final RelatorioService relatorioService;

    public DeletaRelatorioUseCase(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    public UUID executar(UUID idRelatorio) {

        Relatorio relatorioEncontrado = this.relatorioService.findById(idRelatorio);

        if (relatorioEncontrado == null) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id " + idRelatorio);

        if (!relatorioEncontrado.getCliente().getId()
                .equals(SecurityUtil.buscaIdClienteAutenticado())) {
            throw new FalhaAutenticacaoException(
                    "Não é permitido que um cliente delete um relatório que não pertence a ele!");
        }

        this.relatorioService
                .verificaAutorizacaoClienteParaAlterarRelatorio(relatorioEncontrado);

        return this.relatorioService.deletarPorId(idRelatorio);
    }
}
