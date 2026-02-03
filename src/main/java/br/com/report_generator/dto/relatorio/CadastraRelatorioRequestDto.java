package br.com.report_generator.dto.relatorio;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastraRelatorioRequestDto(
        @NotNull
        String titulo,
        String subtitulo,
        @NotNull
        String nome,
        String informacao,
        String descricaoTecnica,
        String descricaoVersao,
        @NotNull
        UUID idSistema
) {
}
