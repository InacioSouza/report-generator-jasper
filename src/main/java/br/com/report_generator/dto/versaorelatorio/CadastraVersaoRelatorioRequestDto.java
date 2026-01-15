package br.com.report_generator.dto.versaorelatorio;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastraVersaoRelatorioRequestDto(
        @NotNull
        UUID idRelatorio,
        String descricaoVersao
) {
}
