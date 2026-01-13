package br.com.report_generator.infra.factor;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.model.TipoArquivoEnum;
import br.com.report_generator.model.VersaoRelatorio;

import java.util.ArrayList;
import java.util.Date;

public class VersaoRelatorioFactor {

    private VersaoRelatorio versaoRelatorio;

    public VersaoRelatorioFactor constroiBaseComDescricaoViaDTO(CadastraRelatorioDto dto) {
        this.versaoRelatorio = new VersaoRelatorio();
        this.versaoRelatorio.setDescricaoVersao(dto.descricaoVersao());
        versaoRelatorio.setDataCriacao(new Date());
        versaoRelatorio.setTipoArquivo(TipoArquivoEnum.JRXML);
        versaoRelatorio.setTipoFinalRelatorio(TipoArquivoEnum.PDF);
        versaoRelatorio.setListSubreport(new ArrayList<>());

        return this;
    }

    public VersaoRelatorio build() {
        return this.versaoRelatorio;
    }
}
