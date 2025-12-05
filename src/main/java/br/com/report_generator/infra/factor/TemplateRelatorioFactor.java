package br.com.report_generator.infra.factor;

import br.com.report_generator.dto.CadastraTemplateDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.TemplateRelatorio;

public class TemplateRelatorioFactor {

    private TemplateRelatorio template;

    public TemplateRelatorioFactor constroiTemplateUtilizandoDto(CadastraTemplateDto dto) {
        TemplateRelatorio template = new TemplateRelatorio();
        template.setTitulo(dto.titulo());
        template.setSubtitulo(dto.subtitulo());
        template.setNomeTemplate(dto.nomeTemplate());
        template.setInformacaoRelatorio(dto.informacaoRelatorio());
        template.setDescricaoTemplate(dto.descricaoTemplate());
        return this;
    }

    public TemplateRelatorioFactor addSistema(Sistema sistema) {
        if (this.template != null) {
            this.template.setSistema(sistema);
        }
        return this;
    }

    public TemplateRelatorio build() {
        return this.template;
    }
}
