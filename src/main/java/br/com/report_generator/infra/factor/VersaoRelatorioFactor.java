package br.com.report_generator.infra.factor;

import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.TipoArquivoEnum;
import br.com.report_generator.model.VersaoRelatorio;

import java.util.ArrayList;
import java.util.Date;

public class VersaoRelatorioFactor {

    private VersaoRelatorio versaoRelatorio;

    public VersaoRelatorioFactor constriBasico() {
        this.versaoRelatorio = new VersaoRelatorio();
        versaoRelatorio.setDataCriacao(new Date());
        versaoRelatorio.setTipoArquivo(TipoArquivoEnum.JRXML);
        versaoRelatorio.setTipoFinalRelatorio(TipoArquivoEnum.PDF);
        versaoRelatorio.setListSubreport(new ArrayList<>());

        return this;
    }

    public VersaoRelatorioFactor constroiPadraoComDescricaoViaDTO(CadastraRelatorioRequestDto dto) {
        this.constriBasico();
        this.versaoRelatorio.setDescricaoVersao(dto.descricaoVersao());
        return this;
    }

    public VersaoRelatorioFactor constroiComCadastraVersaoRelatorioRequestDto(
            CadastraVersaoRelatorioRequestDto dto
    ) {
        this.constriBasico();
        this.versaoRelatorio.setDescricaoVersao(dto.descricaoVersao());
        return this;
    }

    public VersaoRelatorioFactor addRelatorio(Relatorio relatorio) {
        if(versaoRelatorio != null) this.versaoRelatorio.setRelatorio(relatorio);
        return this;
    }

    public VersaoRelatorio build() {
        return this.versaoRelatorio;
    }
}
