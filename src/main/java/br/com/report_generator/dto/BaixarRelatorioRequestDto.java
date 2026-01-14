package br.com.report_generator.dto;

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
