package br.com.report_generator.infra.factor;

import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.Template;
import br.com.report_generator.model.dto.template.TemplateUploadDto;

public class TemplateRelatorioFactor {

    private Template template;

    public TemplateRelatorioFactor constroiTemplateUtilizandoDto(TemplateUploadDto dto) {
        Template template = new Template();
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

    public TemplateRelatorioFactor addArquivoOriginal(byte[] arquivoOriginal) {
        if (this.template != null) {
            this.template.setArquivoOriginal(arquivoOriginal);
        }
        return this;
    }

    public TemplateRelatorioFactor addArquivoCompilado(byte[] arquivoCompilado) {
        if (this.template != null) {
            this.template.setArquivoCompilado(arquivoCompilado);
        }
        return this;
    }

    public Template build() {
        return this.template;
    }
}
