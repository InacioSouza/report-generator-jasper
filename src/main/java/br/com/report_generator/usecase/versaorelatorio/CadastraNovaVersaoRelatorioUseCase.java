package br.com.report_generator.usecase.versaorelatorio;

import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioResponseDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import org.springframework.web.multipart.MultipartFile;

public class CadastraNovaVersaoRelatorioUseCase {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;

    public CadastraNovaVersaoRelatorioUseCase(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
    }

    public VersaoRelatorioResponseDto executar(MultipartFile versaoZip,
                                               CadastraVersaoRelatorioRequestDto infos) {

        Relatorio relatorio = this.relatorioService.findById(infos.idRelatorio());
        if (relatorio == null) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id: " + infos.idRelatorio()
        );

        this.relatorioService
                .verificaAutorizacaoClienteParaAlterarRelatorio(relatorio);

        return this.versaoRelatorioService
                .cadastraVersaoRelatorio(versaoZip, infos, relatorio);
    }
}
