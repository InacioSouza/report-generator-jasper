package br.com.report_generator.dto.relatorio;

import java.util.UUID;

public record AtualizaRelatorioRequestDto(
        UUID idRelatorio,
        String tituloPadrao,
        String subtituloPadrao,
        String nome,
        String informacao,
        String descricaoTecnica,
        UUID idSistema
) {
}
