package br.com.report_generator.service;

import br.com.report_generator.dto.CadastraTemplateDto;
import br.com.report_generator.infra.exception.FalhaAoSalvarTemplateException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.infra.factor.TemplateRelatorioFactor;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.TemplateRelatorio;
import br.com.report_generator.repository.TemplateRelatorioRepository;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.api.TemplateService;
import br.com.report_generator.service.generic.GenericServiceImpl;
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
import java.util.UUID;

@Service("br.com.report_generator.service.TemplateServiceImpl")
public class TemplateRelatorioServiceImpl extends GenericServiceImpl<TemplateRelatorio, UUID> implements TemplateService  {

    private final TemplateRelatorioRepository repository;

    @Autowired
    private SistemaService sistemaService;

    TemplateRelatorioServiceImpl(TemplateRelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public TemplateRelatorio uploadTemplate(CadastraTemplateDto templateUploadDto) {

        Sistema sistemaEncontrado = this.sistemaService.findById(templateUploadDto.sistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException("Não foi encontrado sistema parao id : " + templateUploadDto.sistema().id());
        }

        TemplateRelatorio template = new TemplateRelatorioFactor()
                .constroiTemplateUtilizandoDto(templateUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

        this.trataBytesDoRelatorio(templateUploadDto, template);

        return repository.save(template);
    }

    private void trataBytesDoRelatorio(CadastraTemplateDto templateUploadDto, TemplateRelatorio templateRelatorio) {
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
    }
}
