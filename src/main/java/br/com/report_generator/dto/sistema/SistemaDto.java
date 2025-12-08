package br.com.report_generator.dto.sistema;

import br.com.report_generator.model.Sistema;

public record SistemaDto(
        Long id,
        String nome,
        String descricao
) {

    public SistemaDto(Sistema sistema) {
        this(sistema.getId(), sistema.getNome(), sistema.getDescricao());
    }

}
