package br.com.report_generator.service.api;

import br.com.report_generator.dto.relatorio.BaixarRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.generic.GenericService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface RelatorioService extends GenericService<Relatorio, UUID> {

    RelatorioCadastradoResponseDto uploadRelatorio(MultipartFile arquivo, CadastraRelatorioRequestDto relatorioUploadDto);
    void baixarRelatorio(BaixarRelatorioRequestDto dto, HttpServletResponse response);
}
