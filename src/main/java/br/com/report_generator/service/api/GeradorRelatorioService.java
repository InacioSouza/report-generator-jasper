package br.com.report_generator.service.api;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDto;
import br.com.report_generator.model.VersaoRelatorio;

public interface GeradorRelatorioService {
    PdfGeradoDto gerarRelatorio(
            GeraRelatorioRequestDto pedidoDTO,
            VersaoRelatorio versaoRelatorio,
            String nomeFinalPDF
    );
}
