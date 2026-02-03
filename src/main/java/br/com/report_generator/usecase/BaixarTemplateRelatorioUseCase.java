package br.com.report_generator.usecase;

import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.utils.ZipUtil;
import br.com.report_generator.dto.relatorio.BaixarRelatorioRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BaixarTemplateRelatorioUseCase {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;
    private final ArquivoSubreportService arquivoSubreportService;

    public BaixarTemplateRelatorioUseCase(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService,
            ArquivoSubreportService arquivoSubreportService
    ) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
        this.arquivoSubreportService = arquivoSubreportService;
    }

    /**
     * Baixa os templates JRXML referentes a uma versão específica do Relatorio.
     * Os templates são compactados em um aqrquivo '.zip' com o nome do relatório.
     * O arquivo '.zip' é escrito direto no objeto HttpServletResponse da requisição.
     * @param dto BaixarRelatorioRequestDto
     * @param httpResponse HttpServletResponse
     */
    public void executar(
            BaixarRelatorioRequestDto dto,
            HttpServletResponse httpResponse
    ) {

        Relatorio relatorioEncontrado = this.relatorioService.findById(dto.idRelatorio());
        if (relatorioEncontrado == null) throw new RegistroNaoEncontradoException("Não foi encontrado relatório para o id: " + dto.idRelatorio());

        this.relatorioService
                .verificaAutorizacaoSistemaParaAlterarRelatorio(relatorioEncontrado);

        VersaoRelatorio versaoRelatorioEncontrada;
        if(dto.numeroVersao() == null) {
            versaoRelatorioEncontrada = this.versaoRelatorioService.buscaVersaoRelatorioMaisRecentePara(dto.idRelatorio());
        } else {
            versaoRelatorioEncontrada = this.versaoRelatorioService.buscaVersaoRelatorioPorIdRelatorio(dto.idRelatorio(), dto.numeroVersao());
        }

        if (versaoRelatorioEncontrada == null) throw new RegistroNaoEncontradoException("Não foi encontrado versão de relatório para o número: " + dto.numeroVersao());


        List<ArquivoSubreport> listSubreports = this.arquivoSubreportService
                .buscarSubreportsPorVersao(versaoRelatorioEncontrada.getId());


        Map<String, byte[]> mapArquivos = new HashMap<>();
        mapArquivos.put(versaoRelatorioEncontrada.getNomeArquivo(), versaoRelatorioEncontrada.getArquivoOriginal());

        for(ArquivoSubreport arquivoSubreport : listSubreports) {
            mapArquivos.put(arquivoSubreport.getNomeParametro(), arquivoSubreport.getArquivoOriginal());
        }

        byte[] zipArquivos = ZipUtil.gerarZip(mapArquivos);

        try {
            httpResponse.getOutputStream().write(zipArquivos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String nomeZip = relatorioEncontrado.getNome() + ".zip";

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentType("application/zip");
        httpResponse.setHeader("Content-Disposition", dto.exibicao().getExibicao() + "; filename=\"" + nomeZip + "\"");

    }
}
