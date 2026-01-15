package br.com.report_generator.dto.relatorio;

import br.com.report_generator.dto.ObtencaoArquivoEnum;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BaixarRelatorioRequestDto(
        @NotNull
        UUID idRelatorio,
        Integer numeroVersao,
        @NotNull
        ObtencaoArquivoEnum exibicao
) {
}
