package br.com.report_generator.service.api;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDto;

public interface GeradorRelatorioService {
    PdfGeradoDto gerarRelatorio(GeraRelatorioRequestDto pedidoDTO);
}
