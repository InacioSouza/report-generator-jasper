package br.com.report_generator.service;

import br.com.report_generator.dto.GenerateReportRequestDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.GeneratorReportService;
import br.com.report_generator.service.api.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("br.com.report_generator.service.GeneratorReportServiceImpl")
public class GeneratorReportServiceImpl implements GeneratorReportService {

    @Autowired
    private RelatorioService relatorioService;

    @Override
    public byte[] generateReport(GenerateReportRequestDto reportRequestDto) throws Exception {

        /*
        TemplateRelatorio templateEncontrado = this.buscaTemplate(reportRequestDto.templateId());

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

        return JasperExportManager.exportReportToPdf(print); */

        return null;
    }

    private Relatorio buscaTemplate(UUID id) {
        Relatorio template  = this.relatorioService.findById(id);
        return template;
    }
}
