package br.com.report_generator.dto;

import br.com.report_generator.model.VersaoRelatorio;

import java.util.UUID;

public record VersaoRelatorioRequestDto(UUID idVersao, UUID idRelatorio, Integer numeroVersao ) {
    public VersaoRelatorioRequestDto(VersaoRelatorio versaoRelatorio) {
        this(versaoRelatorio.getId(), versaoRelatorio.getRelatorio().getId(), versaoRelatorio.getNumeroVersao());
    }
}
