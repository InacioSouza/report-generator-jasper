package br.com.report_generator.service;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.api.RelatorioService;
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

@Service("br.com.report_generator.service.RelatorioServiceImpl")
public class RelatorioServiceImpl extends GenericServiceImpl<Relatorio, UUID> implements RelatorioService {

    private final RelatorioRepository repository;

    @Autowired
    private SistemaService sistemaService;

    RelatorioServiceImpl(RelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Relatorio uploadRelatorio(CadastraRelatorioDto relatorioUploadDto) {

        Sistema sistemaEncontrado = this.sistemaService.findById(relatorioUploadDto.sistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException("Não foi encontrado sistema parao id : " + relatorioUploadDto.sistema().id());
        }

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

        this.trataBytesDoRelatorio(relatorioUploadDto, relatorio);

        return repository.save(relatorio);
    }

    private void trataBytesDoRelatorio(CadastraRelatorioDto relatorioUploadDto, Relatorio relatorio) {
        MultipartFile arquivo = relatorioUploadDto.arquivoOriginal();
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
            throw new FalhaAoSalvarRelatorioException();
        }
    }
}
