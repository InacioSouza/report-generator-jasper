package br.com.report_generator.infra.factor;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.Relatorio;

import java.util.ArrayList;

public class RelatorioFactor {

    private Relatorio relatorio;

    public RelatorioFactor constroiRelatorioUtilizandoDto(CadastraRelatorioDto dto) {
        this.relatorio = new Relatorio();
        this.relatorio.setTitulo(dto.titulo());
        this.relatorio.setSubtitulo(dto.subtitulo());
        this.relatorio.setNome(dto.nome());
        this.relatorio.setInformacao(dto.informacao());
        this.relatorio.setDescricaoTecnica(dto.descricaoTecnica());
        this.relatorio.setListVersoes(new ArrayList<>());
        return this;
    }

    public RelatorioFactor addSistema(Sistema sistema) {
        if (this.relatorio != null) {
            this.relatorio.setSistema(sistema);
        }
        return this;
    }

    public Relatorio build() {
        return this.relatorio;
    }
}
