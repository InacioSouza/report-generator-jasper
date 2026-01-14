package br.com.report_generator.service.api;

import br.com.report_generator.dto.*;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.generic.GenericService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface RelatorioService extends GenericService<Relatorio, UUID> {

    Relatorio uploadRelatorio(MultipartFile arquivo, CadastraRelatorioDto relatorioUploadDto);
    PdfGerado gerarRelatorio(PedidoRelatorioDTO pedidoDTO);
    void baixarRelatorio(BaixarRelatorioRequestDto dto, HttpServletResponse response);
}
