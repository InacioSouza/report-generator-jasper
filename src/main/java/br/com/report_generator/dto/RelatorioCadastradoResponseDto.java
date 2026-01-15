package br.com.report_generator.dto;

import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;

import java.util.UUID;

public record RelatorioCadastradoResponseDto(
        UUID idRelatorio,
        UUID idVersao,
        Integer numeroVersao

) {
    public RelatorioCadastradoResponseDto(Relatorio relatorio, VersaoRelatorio versaoRelatorio) {
        this(relatorio.getId(), versaoRelatorio.getId(), versaoRelatorio.getNumeroVersao());
    }
}
