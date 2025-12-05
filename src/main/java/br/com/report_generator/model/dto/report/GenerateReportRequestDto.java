package br.com.report_generator.model.dto.report;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record GenerateReportRequestDto(
        @NotNull
        String templateId,
        Map<String, Object> parameters,
        @NotNull
        @NotEmpty
        List<Map<String, Object>> data
) {
}
