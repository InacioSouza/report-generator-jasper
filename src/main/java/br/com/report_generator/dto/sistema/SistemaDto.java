package br.com.report_generator.dto.sistema;

import jakarta.validation.constraints.NotNull;

public record SistemaDto(
        @NotNull
        Integer id,
        String nome,
        String descricao
) {
}
