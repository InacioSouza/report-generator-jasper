package br.com.report_generator.dto;

import jakarta.validation.constraints.NotNull;

public record CadastraRelatorioDto(
        @NotNull
        String titulo,
        String subtitulo,
        String nome,
        String informacao,
        String descricaoTecnica,
        String descricaoVersao,
        @NotNull
        SistemaDto sistema
) {
}
