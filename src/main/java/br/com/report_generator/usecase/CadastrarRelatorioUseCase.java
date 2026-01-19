package br.com.report_generator.usecase;

import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CadastrarRelatorioUseCase {

    private final SistemaService sistemaService;
    private final RelatorioService relatorioService;

    public CadastrarRelatorioUseCase(
            SistemaService sistemaService,
            RelatorioService relatorioService
    ) {
        this.sistemaService = sistemaService;
        this.relatorioService = relatorioService;
    }

    public RelatorioCadastradoResponseDto executar(
            MultipartFile file,
            CadastraRelatorioRequestDto infos
    ) {

        Sistema sistemaEncontrado = this.sistemaService.findById(infos.idSistema());
        if (sistemaEncontrado == null) {
            throw new RegistroNaoEncontradoException(
                    "Não foi encontrado sistema para o id : " + infos.idSistema()
            );
        }

        return this.relatorioService.uploadRelatorio(file, infos, sistemaEncontrado);
    }
}
