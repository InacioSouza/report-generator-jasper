package br.com.report_generator.service.api;

import br.com.report_generator.model.dto.report.GenerateReportRequestDto;

import java.io.IOException;

public interface GeneratorReportService {
    byte[] generateReport(GenerateReportRequestDto reportRequestDto) throws Exception;
}
