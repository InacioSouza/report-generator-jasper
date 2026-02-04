package br.com.report_generator.dto.filtros;

import java.util.Date;
import java.util.UUID;

public record VersaoRelatorioFiltroDto(
        UUID id,
        String nome,
        String nomeArquivo,
        UUID idRelatorio,
        Integer numeroVersao,
        String descricaoVersao,
        Date dataCriacaoInicio,
        Date dataCriacaoFim,
        Date ultimaAtualizacao,
        UUID idSistema
) {
}
