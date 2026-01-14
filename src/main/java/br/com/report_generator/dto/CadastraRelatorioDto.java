package br.com.report_generator.dto;

import jakarta.validation.constraints.NotNull;

public record CadastraRelatorioDto(
        @NotNull
        String titulo,
        String subtitulo,
        @NotNull
        String nome,
        String informacao,
        String descricaoTecnica,
        String descricaoVersao,
        @NotNull
        Integer idSistema
) {
}
