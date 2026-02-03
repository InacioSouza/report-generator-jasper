package br.com.report_generator.dto.filtros;

import java.util.Date;
import java.util.UUID;

public record RelatorioFiltroDto (
        UUID idRelatorio,
        String nomeRelatorio,
        String tituloPadrao,
        String subtituloPadrao,
        String informacao,
        String descricaoTecnica,
        UUID idSistema,
        Integer numeroUltimaVersao,
        Date dataCriacaoInicio,
        Date dataCriacaoFim
) {

}
