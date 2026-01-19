package br.com.report_generator.service.api;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.generic.GenericService;

public interface SistemaService extends GenericService<Sistema, Long> {
    SistemaResponseDto atualiza(Long id, SistemaRequestDto dto);
}
