package br.com.report_generator.repository;

import br.com.report_generator.contexto.IntegrationTestContext;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.TipoArquivoEnum;
import br.com.report_generator.model.VersaoRelatorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VersaoRelatorioRepositoryTest extends IntegrationTestContext {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private VersaoRelatorioRepository versaoRelatorioRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    private Relatorio relatorio;
    private VersaoRelatorio versao0;
    private VersaoRelatorio versao1;

    @BeforeEach
    void setup() throws Exception {

        Sistema sistema;

        sistema = new Sistema("RH", "Recursos Humanos");

        this.sistemaRepository.save(sistema);

        this.relatorio = new Relatorio();
        this.relatorio.setSistema(sistema);
        this.relatorio.setTituloPadrao("Relatório de Funcionários");
        this.relatorio.setDataCriacao(new Date());
        this.relatorio.setUltimaAtualizacao(new Date());
        this.relatorio.setListVersoes(new ArrayList<>());
        this.relatorio.setNumeroUltimaVersao(1);

        this.relatorioRepository.save(this.relatorio);

        byte[] templateJRXML = new ClassPathResource(
                "/templatesjasper/relatorio-veiculos-pessoa-main.jrxml")
                .getInputStream().readAllBytes();


        this.versao0 = new VersaoRelatorio();
        this.versao0.setNomeArquivo("relatorio-veiculos-pessoa-main.jrxml");
        this.versao0.setRelatorio(this.relatorio);
        this.versao0.setNumeroVersao(1);
        this.versao0.setTipoArquivo(TipoArquivoEnum.JRXML);
        this.versao0.setTipoFinalRelatorio(TipoArquivoEnum.PDF);
        this.versao0.setArquivoOriginal(templateJRXML);
        this.versao0.setDataCriacao(new Date());
        this.versao0.setUltimaAtualizacao(new Date());

        this.versaoRelatorioRepository.save(this.versao0);

        this.versao1 = new VersaoRelatorio();
        this.versao1.setNomeArquivo("relatorio-veiculos-pessoa-main.jrxml");
        this.versao1.setRelatorio(this.relatorio);
        this.versao1.setNumeroVersao(2);
        this.versao1.setTipoArquivo(TipoArquivoEnum.JRXML);
        this.versao1.setTipoFinalRelatorio(TipoArquivoEnum.PDF);
        this.versao1.setArquivoOriginal(templateJRXML);
        this.versao1.setDataCriacao(new Date());
        this.versao1.setUltimaAtualizacao(new Date());

        this.versaoRelatorioRepository.save(this.versao1);

    }

    @DisplayName("Teste busca versão relatorio mais recente para o relatorio")
    @Test
    void buscaVersaoRelatorioMaisRecenteParaRelatorio() {

        Optional<VersaoRelatorio> optionalVersaoRelatorio = this.versaoRelatorioRepository
                .buscaVersaoRelatorioMaisRecentePara(this.relatorio.getId());

        assertTrue(optionalVersaoRelatorio.isPresent());

        VersaoRelatorio ultimaVersao = optionalVersaoRelatorio.get();

        assertEquals(this.versao1.getId(), ultimaVersao.getId());
    }

    @DisplayName("Teste Busca Versão Relatório Por Id Relatório E Número Versão")
    @Test
    void testBuscaVersaoRelatorioPorIdRelatorioENumeroVersao() {
        Optional<VersaoRelatorio> optionalVersaoRelatorio = this.versaoRelatorioRepository
                .buscaVersaoRelatorioPorIdRelatorioENumeroVersao(
                        this.relatorio.getId(),
                        this.versao0.getNumeroVersao());

        assertTrue(optionalVersaoRelatorio.isPresent());
        assertEquals(this.versao0.getId(), optionalVersaoRelatorio.get().getId());
    }

}