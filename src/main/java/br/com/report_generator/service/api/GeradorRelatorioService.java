package br.com.report_generator.service.api;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDTO;

public interface GeradorRelatorioService {
    PdfGeradoDto gerarRelatorio(GeraRelatorioRequestDTO pedidoDTO);
}
