package br.com.report_generator.service.api;

import br.com.report_generator.model.dto.template.TemplateUploadDto;
import br.com.report_generator.model.Template;

public interface TemplateService {
    Template uploadTemplate(TemplateUploadDto templateUploadDto);
}
