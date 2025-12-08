package br.com.report_generator.service.api;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.UUID;

public interface RelatorioService extends GenericService<Relatorio, UUID> {
    Relatorio uploadRelatorio(CadastraRelatorioDto relatorioUploadDto);
}
