package br.com.report_generator.dto;

import br.com.report_generator.model.Sistema;

import java.util.UUID;

public record SistemaResponseDto(
        UUID id,
        String nome,
        String descricao
) {

    public SistemaResponseDto(Sistema sistema) {
        this(sistema.getId(), sistema.getNome(), sistema.getDescricao());
    }

}
