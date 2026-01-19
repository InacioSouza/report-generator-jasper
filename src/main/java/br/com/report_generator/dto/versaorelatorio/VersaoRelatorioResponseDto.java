package br.com.report_generator.dto.versaorelatorio;

import br.com.report_generator.model.VersaoRelatorio;

import java.util.UUID;

public record VersaoRelatorioResponseDto(UUID idVersao, UUID idRelatorio, Integer numeroVersao ) {
    public VersaoRelatorioResponseDto(VersaoRelatorio versaoRelatorio) {
        this(versaoRelatorio.getId(), versaoRelatorio.getRelatorio().getId(), versaoRelatorio.getNumeroVersao());
    }
}
