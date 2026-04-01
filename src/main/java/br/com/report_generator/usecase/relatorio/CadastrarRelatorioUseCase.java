package br.com.report_generator.usecase.relatorio;

import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.infra.exception.FalhaAutenticacaoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Cliente;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.ClienteService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.utils.SecurityUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CadastrarRelatorioUseCase {

    private final SistemaService sistemaService;
    private final RelatorioService relatorioService;
    private final ClienteService clienteService;

    public CadastrarRelatorioUseCase(
            SistemaService sistemaService,
            RelatorioService relatorioService,
            ClienteService clienteService
    ) {
        this.sistemaService = sistemaService;
        this.relatorioService = relatorioService;
        this.clienteService = clienteService;
    }

    public RelatorioCadastradoResponseDto executar(
            MultipartFile file,
            CadastraRelatorioRequestDto infos
    ) {

        if (!infos.idCliente().equals(SecurityUtil.buscaIdClienteAutenticado())) {
            throw new FalhaAutenticacaoException(
                    "Não é permitido que um cliente cadastre um relatório com o id de outro cliente!");
        }

        Cliente clienteEncontrado = this.clienteService.findById(infos.idCliente());
        if (clienteEncontrado == null) {
            throw new RegistroNaoEncontradoException(
                    "Não foi encontrado cliente para o id : " + infos.idSistema()
            );
        }

        Sistema sistemaEncontrado = this.sistemaService.findById(infos.idSistema());
        if (sistemaEncontrado == null) {
            throw new RegistroNaoEncontradoException(
                    "Não foi encontrado sistema para o id : " + infos.idSistema()
            );
        }

        return this.relatorioService.uploadRelatorio(
                file,
                infos,
                sistemaEncontrado,
                clienteEncontrado
        );
    }
}
