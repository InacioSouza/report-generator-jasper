package br.com.report_generator.service;

import br.com.report_generator.dto.PdfGeradoDto;
import br.com.report_generator.dto.relatorio.GeraRelatorioRequestDto;
import br.com.report_generator.infra.exception.FalhaAoGerarRelatorioException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.GeradorRelatorioService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.utils.JasperUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("br.com.report_generator.service.GeneratorReportServiceImpl")
public class GeradorRelatorioServiceImpl implements GeradorRelatorioService {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private VersaoRelatorioService versaoRelatorioService;

    @Autowired
    private ArquivoSubreportService arquivoSubreportService;

    @Override
    public PdfGeradoDto gerarRelatorio(GeraRelatorioRequestDto pedidoDTO) {

        Relatorio relatorio = this.relatorioService.findById(pedidoDTO.idRelatorio());

        if(pedidoDTO.dataSource().isEmpty()) {
            throw new IllegalArgumentException("O dataSource não pode estar vazio!");
        }

        if(relatorio == null) {
            throw new RegistroNaoEncontradoException("Não existe relatório para o id : " + pedidoDTO.idRelatorio());
        }

        VersaoRelatorio versaoRelatorio =  this.versaoRelatorioService.findById(pedidoDTO.idVersao());

        if(versaoRelatorio == null) {
            throw new RegistroNaoEncontradoException("Não existe versão de relatório para o id : " + pedidoDTO.idVersao());
        }

        byte[] templateCompilado = versaoRelatorio.getArquivoCompilado() != null ?
                versaoRelatorio.getArquivoCompilado() : JasperUtil.compilaJRXML(versaoRelatorio.getArquivoOriginal());

        InputStream inputStreamRelatorio = new ByteArrayInputStream(templateCompilado);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pedidoDTO.dataSource());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_PADRAO", pedidoDTO.titulo());
        parametros.put("SUBTITULO_PADRAO", pedidoDTO.subtitulo());

        // Parâmetro padrão exigido quando não há SQL
        parametros.putIfAbsent(JRParameter.REPORT_DATA_SOURCE, dataSource);

        List<ArquivoSubreport> listSubreports = this.arquivoSubreportService.buscarSubreportsPorVersao(pedidoDTO.idVersao());

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            for(ArquivoSubreport arquivoSubreport : listSubreports) {
                byte[] templateSubrelatorioCompilado = arquivoSubreport.getArquivoCompilado() != null ?
                        arquivoSubreport.getArquivoCompilado() : JasperUtil.compilaJRXML(arquivoSubreport.getArquivoOriginal());

                parametros.put(arquivoSubreport.getNomeParametro(), JRLoader.loadObject(new ByteArrayInputStream(templateSubrelatorioCompilado)));
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStreamRelatorio, parametros, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        } catch (JRException e) {
            e.printStackTrace();
            throw new FalhaAoGerarRelatorioException();
        }

        String nomeRelatorio = relatorio.getNome() + "-v-" + versaoRelatorio.getNumeroVersao() + ".pdf";

        return new PdfGeradoDto(nomeRelatorio, out.toByteArray());
    }
}
