package br.com.report_generator.usecase;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.GeradorRelatorioService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import org.springframework.stereotype.Component;

@Component
public class GeraRelatorioUseCase {

    private final RelatorioService relatorioService;
    private final VersaoRelatorioService versaoRelatorioService;
    private final GeradorRelatorioService geradorRelatorioService;

    public GeraRelatorioUseCase(
            RelatorioService relatorioService,
            VersaoRelatorioService versaoRelatorioService,
            GeradorRelatorioService geradorRelatorioService
    ) {
        this.relatorioService = relatorioService;
        this.versaoRelatorioService = versaoRelatorioService;
        this.geradorRelatorioService = geradorRelatorioService;
    }

    public PdfGeradoDto executar(
            GeraRelatorioRequestDto pedidoDTO
    ) {

        Relatorio relatorio = this.relatorioService.findById(pedidoDTO.idRelatorio());

        this.relatorioService
                .verificaAutorizacaoSistemaParaAlterarRelatorio(relatorio);

        if(pedidoDTO.dataSource().isEmpty()) {
            throw new IllegalArgumentException("O dataSource não pode estar vazio!");
        }

        if(relatorio == null) {
            throw new RegistroNaoEncontradoException("Não existe relatório para o id : " + pedidoDTO.idRelatorio());
        }

        VersaoRelatorio versaoRelatorio =  this.versaoRelatorioService.findById(pedidoDTO.idVersao());

        if(versaoRelatorio == null) {
            throw new RegistroNaoEncontradoException("Não existe versão de relatório para o id : " + pedidoDTO.idVersao());
        }

        String nomeRelatorioPDF = relatorio.getNome() + "-v-" + versaoRelatorio.getNumeroVersao() + ".pdf";

        return this.geradorRelatorioService.gerarRelatorio(
                pedidoDTO,
                versaoRelatorio,
                nomeRelatorioPDF
        );
    }
}
