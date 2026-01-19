package br.com.report_generator.dto;

import br.com.report_generator.model.Sistema;

public record SistemaResponseDto(
        Long id,
        String nome,
        String descricao
) {

    public SistemaResponseDto(Sistema sistema) {
        this(sistema.getId(), sistema.getNome(), sistema.getDescricao());
    }

}
