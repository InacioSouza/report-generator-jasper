package br.com.report_generator.service;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service("br.com.report_generator.service.RelatorioServiceImpl")
public class RelatorioServiceImpl extends GenericServiceImpl<Relatorio, UUID> implements RelatorioService {

    private final RelatorioRepository repository;

    @Autowired
    private SistemaService sistemaService;

    RelatorioServiceImpl(RelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    private void validaArquivoRecebido(MultipartFile arquivo) {
        if(arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename() == "") {
            throw new FormatoArquivoInvalidoException("O arquivo enviado não possui nome!");
        }

        if(arquivo.isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado está vazio!");
        }
    }

    private Map<String, byte[]> extrairArquivos(MultipartFile arquivo) {

        Map<String, byte[]> map = new HashMap<>();

        try(ZipInputStream zis = new ZipInputStream(arquivo.getInputStream())) {

            ZipEntry entry;

            while((entry = zis.getNextEntry()) != null ) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                zis.transferTo(baos);
                map.put(entry.getName(), baos.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair .zip", e);
        }

        return map;
    }

    @Override
    public Relatorio uploadRelatorio(MultipartFile arquivo, CadastraRelatorioDto relatorioUploadDto) {

        this.validaArquivoRecebido(arquivo);

        Sistema sistemaEncontrado = this.sistemaService.findById(relatorioUploadDto.sistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException("Não foi encontrado sistema para o id : " + relatorioUploadDto.sistema().id());
        }

        Map<String, byte[]> mapArquivos = this.extrairArquivos(arquivo);

        if (mapArquivos.size() <= 2) {

        } else {

        }

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

        this.trataBytesDoRelatorio(relatorioUploadDto, relatorio);

        return repository.save(relatorio);
    }

    private void trataBytesDoRelatorio(CadastraRelatorioDto relatorioUploadDto, Relatorio relatorio) {
        MultipartFile arquivo = null;
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
