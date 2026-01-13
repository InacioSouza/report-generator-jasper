package br.com.report_generator.service.api;

import br.com.report_generator.dto.GenerateReportRequestDto;

public interface GeneratorReportService {
    byte[] generateReport(GenerateReportRequestDto reportRequestDto) throws Exception;
}
