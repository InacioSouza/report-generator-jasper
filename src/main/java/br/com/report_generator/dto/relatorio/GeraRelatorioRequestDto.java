package br.com.report_generator.dto.relatorio;

import br.com.report_generator.dto.ObtencaoArquivoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GeraRelatorioRequestDto(
        @NotNull @NotBlank
        UUID idRelatorio,
        @NotNull @NotBlank
        UUID idVersao,
        @NotNull
        List<Map<String, Object>> dataSource,
        List<ParametroRelatorioDto> parametros,
        @NotNull
        ObtencaoArquivoEnum exibicaoRelatorio) {
}
