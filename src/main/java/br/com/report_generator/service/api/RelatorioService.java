package br.com.report_generator.service.api;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.dto.PdfGerado;
import br.com.report_generator.dto.PedidoRelatorioDTO;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface RelatorioService extends GenericService<Relatorio, UUID> {
    Relatorio uploadRelatorio(MultipartFile arquivo, CadastraRelatorioDto relatorioUploadDto);
    PdfGerado gerarRelatorio(PedidoRelatorioDTO pedidoDTO);
}
