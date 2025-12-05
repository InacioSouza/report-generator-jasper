package br.com.report_generator.service;

import br.com.report_generator.infra.exception.FalhaAoSalvarTemplateException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.Template;
import br.com.report_generator.model.dto.template.TemplateUploadDto;
import br.com.report_generator.model.factor.TemplateFactor;
import br.com.report_generator.repository.TemplateRepository;
import br.com.report_generator.service.api.TemplateService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service("br.com.report_generator.service.TemplateServiceImpl")
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository repository;

    @Override
    public Template uploadTemplate(TemplateUploadDto templateUploadDto) {

        MultipartFile arquivo = templateUploadDto.arquivoOriginal();
        byte[] bytesOriginal = {};
        byte[] bytesCompilado = {};

        try{
            bytesOriginal = arquivo.getBytes();

            String nomeArquivo = arquivo.getOriginalFilename();
            if(nomeArquivo != null && nomeArquivo.endsWith(".jrxml")) {
                JasperReport jasperReport = JasperCompileManager.compileReport(
                        new ByteArrayInputStream(bytesOriginal)
                );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                JRSaver.saveObject(jasperReport, baos);
                bytesCompilado = baos.toByteArray();

            } else if(nomeArquivo != null && nomeArquivo.endsWith(".jasper")) {
                bytesCompilado = bytesOriginal;
            } else {
                throw new FormatoArquivoInvalidoException();
            }

        } catch (IOException | JRException e) {
            throw new FalhaAoSalvarTemplateException();
        }

        Sistema sistema = new Sistema();

        Template template = new TemplateFactor()
                .constroiTemplateUtilizandoDto(templateUploadDto)
                .addArquivoOriginal(bytesOriginal)
                .addArquivoCompilado(bytesCompilado)
                .addSistema(sistema)
                .build();

        return repository.save(template);
    }
}
