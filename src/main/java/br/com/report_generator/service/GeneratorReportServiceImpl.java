package br.com.report_generator.service;

import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Template;
import br.com.report_generator.dto.report.GenerateReportRequestDto;
import br.com.report_generator.service.api.GeneratorReportService;
import br.com.report_generator.service.api.TemplateService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service("br.com.report_generator.service.GeneratorReportServiceImpl")
public class GeneratorReportServiceImpl implements GeneratorReportService {

    @Autowired
    private TemplateService templateService;

    @Override
    public byte[] generateReport(GenerateReportRequestDto reportRequestDto) throws Exception {

        Template templateEncontrado = this.buscaTemplate(reportRequestDto.templateId());

        if(templateEncontrado == null) {
            throw new RegistroNaoEncontradoException("Não foi encontrado template para o id " + reportRequestDto.templateId());
        }

        if(templateEncontrado.getArquivoOriginal() == null && templateEncontrado.getArquivoCompilado() == null) {
            throw new RegistroNaoEncontradoException("Os bytes referentes ao template não foram encontrados");
        }

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                new ByteArrayInputStream(templateEncontrado.getArquivoCompilado())
        );

        JRDataSource dataSource = new JRBeanCollectionDataSource(reportRequestDto.data());

        JasperPrint print = JasperFillManager.fillReport(
                jasperReport,
                reportRequestDto.parameters(),
                dataSource
        );

        return JasperExportManager.exportReportToPdf(print);
    }

    private Template buscaTemplate(String id) {
        this.templateService = null;
        return null;
    }
}
