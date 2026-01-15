package br.com.report_generator.dto.versaorelatorio;

import br.com.report_generator.model.TipoArquivoEnum;
import br.com.report_generator.model.VersaoRelatorio;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record InfoVersaoRelatorioResponseDto (
        UUID idVersao,
        UUID idRelatorio,
        String nomeArquivo,
        Integer numeroVersao,
        String descricaoVersao,
        TipoArquivoEnum tipoArquivo,
        TipoArquivoEnum tipoFinalRelatorio,
        Date dataCriacao,
        List<InfoArquivoSubreportResponseDto> listSubreport
) {
    public InfoVersaoRelatorioResponseDto(VersaoRelatorio versaoRelatorio) {
        this(
                versaoRelatorio.getId(),
                versaoRelatorio.getRelatorio().getId(),
                versaoRelatorio.getNomeArquivo(),
                versaoRelatorio.getNumeroVersao(),
                versaoRelatorio.getDescricaoVersao(),
                versaoRelatorio.getTipoArquivo(),
                versaoRelatorio.getTipoFinalRelatorio(),
                versaoRelatorio.getDataCriacao(),
                versaoRelatorio.getListSubreport().stream().map(InfoArquivoSubreportResponseDto::new).toList()
        );
    }
}
