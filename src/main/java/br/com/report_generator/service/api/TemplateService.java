package br.com.report_generator.service.api;

import br.com.report_generator.dto.CadastraTemplateDto;
import br.com.report_generator.model.TemplateRelatorio;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.UUID;

public interface TemplateService extends GenericService<TemplateRelatorio, UUID> {
    TemplateRelatorio uploadTemplate(CadastraTemplateDto templateUploadDto);
}
