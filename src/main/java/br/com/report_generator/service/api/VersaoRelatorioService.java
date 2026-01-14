package br.com.report_generator.service.api;

import br.com.report_generator.dto.CadastraVersaoRelatorioDto;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VersaoRelatorioService extends GenericService<VersaoRelatorio, UUID> {
    VersaoRelatorio buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao);
    VersaoRelatorio buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio);
    VersaoRelatorio cadastraVersaoRelatorio(MultipartFile arquivoZip, CadastraVersaoRelatorioDto dto);
}
