package br.com.report_generator.dto.versaorelatorio;

import java.util.UUID;

public record AtualizaVersaoRelatorioRequestDto(
        UUID id,
        String nome,
        String descricao
) {
}
