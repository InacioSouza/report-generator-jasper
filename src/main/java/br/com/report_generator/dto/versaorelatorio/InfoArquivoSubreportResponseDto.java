package br.com.report_generator.dto.versaorelatorio;

import br.com.report_generator.model.ArquivoSubreport;

import java.util.UUID;

public record InfoArquivoSubreportResponseDto(
    Long id,
    String nomeParametro,
    UUID idVersaoRelatorio
) {

    public InfoArquivoSubreportResponseDto(ArquivoSubreport arquivoSubreport) {
        this(
                arquivoSubreport.getId(),
                arquivoSubreport.getNomeParametro(),
                arquivoSubreport.getVersaoRelatorio().getId()
        );
    }
}
