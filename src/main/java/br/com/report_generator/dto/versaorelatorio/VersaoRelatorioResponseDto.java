package br.com.report_generator.dto.versaorelatorio;

import br.com.report_generator.model.VersaoRelatorio;

import java.util.UUID;

public record VersaoRelatorioResponseDto(
        UUID idVersao,
        String nomeVersao,
        UUID idRelatorio,
        String descricaoVersao,
        Integer numeroVersao
) {

    public VersaoRelatorioResponseDto(VersaoRelatorio versaoRelatorio) {
        this(
                versaoRelatorio.getId(),
                versaoRelatorio.getNome(),
                versaoRelatorio.getRelatorio().getId(),
                versaoRelatorio.getDescricaoVersao(),
                versaoRelatorio.getNumeroVersao()
        );
    }
}
