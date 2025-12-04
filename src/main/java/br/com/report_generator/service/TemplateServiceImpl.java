package br.com.report_generator.service;

import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.Template;
import br.com.report_generator.model.dto.template.TemplateUploadDto;
import br.com.report_generator.model.factor.TemplateFactor;
import br.com.report_generator.repository.TemplateRepository;
import br.com.report_generator.service.api.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("TemplateServiceImpl")
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository repository;

    @Override
    public Template uploadTemplate(TemplateUploadDto templateUploadDto) {

        MultipartFile arquivo = templateUploadDto.arquivoOriginal();
        byte[] bytesOriginal = {};

        try{
            bytesOriginal = arquivo.getBytes();
        } catch (IOException e){

        }

        byte[] bytesCompilado = {};

        if(arquivo.getOriginalFilename().endsWith(".jrxml")) {
            // Lógica do Jasper
        } else {
            bytesCompilado = bytesOriginal;
        }

        Sistema sistema = new Sistema();

        Template template = new TemplateFactor()
                .constroiTemplateUtilizandoDto(templateUploadDto)
                .addArquivoOriginal(bytesOriginal)
                .addArquivoCompilado(bytesCompilado)
                .addSistema(sistema)
                .build();

        return null;
    }
}
