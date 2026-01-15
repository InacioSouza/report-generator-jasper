package br.com.report_generator.dto.relatorio;

import br.com.report_generator.dto.versaorelatorio.InfoVersaoRelatorioResponseDto;
import br.com.report_generator.model.Relatorio;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public record InfoRelatorioResponseDto(
    UUID idRelatorio,
    Long idSistema,
    Date dataCriacao,
    String tituloPadrao,
    String subtituloPadrao,
    String nome,
    String informacao,
    String descricaoTecnica,
    List<InfoVersaoRelatorioResponseDto> listVersoes
) {
    public InfoRelatorioResponseDto(Relatorio relatorio) {
        this(
                relatorio.getId(),
                relatorio.getSistema().getId(),
                relatorio.getDataCriacao(),
                relatorio.getTituloPadrao(),
                relatorio.getSubtituloPadrao(),
                relatorio.getNome(),
                relatorio.getInformacao(),
                relatorio.getDescricaoTecnica(),
                relatorio.getListVersoes().stream().map(InfoVersaoRelatorioResponseDto::new).toList()
        );
    }

}
