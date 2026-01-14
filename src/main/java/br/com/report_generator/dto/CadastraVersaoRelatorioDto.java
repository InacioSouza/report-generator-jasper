package br.com.report_generator.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastraVersaoRelatorioDto (
        @NotNull
        UUID idRelatorio,
        String descricaoVersao

) {
}
