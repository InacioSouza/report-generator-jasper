package br.com.report_generator.service.api;

import br.com.report_generator.model.dto.template.TemplateUploadDto;
import br.com.report_generator.model.Template;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.UUID;

public interface TemplateService extends GenericService<Template, UUID> {
    Template uploadTemplate(TemplateUploadDto templateUploadDto);
}
