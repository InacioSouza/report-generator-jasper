package br.com.report_generator.service;

import br.com.report_generator.dto.CadastraRelatorioDto;
import br.com.report_generator.dto.PdfGerado;
import br.com.report_generator.dto.PedidoRelatorioDTO;
import br.com.report_generator.infra.exception.FalhaAoGerarRelatorioException;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.model.*;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.JasperUtil;
import br.com.report_generator.service.utils.ZipUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service("br.com.report_generator.service.RelatorioServiceImpl")
public class RelatorioServiceImpl extends GenericServiceImpl<Relatorio, UUID> implements RelatorioService {

    private final RelatorioRepository repository;

    @Autowired
    private SistemaService sistemaService;

    @Autowired
    private VersaoRelatorioService versaoRelatorioService;

    @Autowired
    private ArquivoSubreportService arquivoSubreportService;

    RelatorioServiceImpl(RelatorioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    private void validaArquivoRecebido(MultipartFile arquivo) {

        if(!ZipUtil.assinaturaDoArquivoCorrespondeZIP(arquivo)) {
            throw new FormatoArquivoInvalidoException("O arquivo deve ter extensão '.zip' !");
        }

        if(arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename().isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado não possui nome!");
        }

        if(arquivo.isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado está vazio!");
        }
    }

    private Map<String, byte[]> extraiArquivosDoZip(MultipartFile arquivo) {
        Map<String, byte[]> mapArquivos = ZipUtil.extrairArquivosDoZip(arquivo);

        if(mapArquivos.isEmpty()) throw new FalhaAoSalvarRelatorioException("Nenhum arquivo foi extraído do zip");

        int qtdArquivosMAIN = 0;
        for(String nomeArquivo : mapArquivos.keySet().stream().toList()) {
            if (nomeArquivo.contains("MAIN")) {
                qtdArquivosMAIN++;
            }
        }

        if (qtdArquivosMAIN > 1) {
            throw new IllegalArgumentException("Não deve haver mais de 1 arquivo com o trecho 'MAIN' no nome!");
        }

        return mapArquivos;
    }

    @Override
    public Relatorio uploadRelatorio(MultipartFile arquivo, CadastraRelatorioDto relatorioUploadDto) {

        this.validaArquivoRecebido(arquivo);

        Sistema sistemaEncontrado = this.sistemaService.findById(relatorioUploadDto.sistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException("Não foi encontrado sistema para o id : " + relatorioUploadDto.sistema().id());
        }

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

        Map<String, byte[]> mapArquivos = this.extraiArquivosDoZip(arquivo);

        VersaoRelatorio versaoRelatorio = new VersaoRelatorio();
        versaoRelatorio.setDescricaoVersao(relatorioUploadDto.descricaoVersao());
        versaoRelatorio.setDataCriacao(new Date());
        versaoRelatorio.setTipoArquivo(TipoArquivoEnum.JRXML);
        versaoRelatorio.setTipoFinalRelatorio(TipoArquivoEnum.PDF);
        versaoRelatorio.setListSubreport(new ArrayList<>());

        if (mapArquivos.size() == 1) {
            versaoRelatorio.setNomeArquivo(mapArquivos.keySet().stream().findFirst().get());

            Optional<byte[]> jrxml = mapArquivos.values().stream().findFirst();
            versaoRelatorio.setArquivoOriginal(jrxml.get());
            versaoRelatorio.setArquivoCompilado(JasperUtil.compilaJRXML(jrxml.get()));

        } else {

            for(Map.Entry<String, byte[]> arquivoEntry : mapArquivos.entrySet()) {

                if (arquivoEntry.getKey().contains("MAIN")) {
                    versaoRelatorio.setNomeArquivo(arquivoEntry.getKey());
                    versaoRelatorio.setArquivoOriginal(arquivoEntry.getValue());
                    versaoRelatorio.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));
                }

                ArquivoSubreport arquivoSubreport = new ArquivoSubreport();
                arquivoSubreport.setNomeParametro(arquivoEntry.getKey());
                arquivoSubreport.setArquivoOriginal(arquivoEntry.getValue());
                arquivoSubreport.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));

                versaoRelatorio.getListSubreport().add(arquivoSubreport);
            }
        }

        relatorio.getListVersoes().add(versaoRelatorio);

        return this.repository.save(relatorio);
    }

    @Override
    public PdfGerado gerarRelatorio(PedidoRelatorioDTO pedidoDTO) {

        Relatorio relatorio = this.findById(pedidoDTO.idRelatorio());

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

        for(ArquivoSubreport arquivoSubreport : listSubreports) {
            // Como vou vincular o subreport ao relatorio principal se os registros não salvam nome do parâmetro?
            // Pelo nome do arquivo

            byte[] templateSubrelatorioCompilado = arquivoSubreport.getArquivoCompilado() != null ?
                    arquivoSubreport.getArquivoCompilado() : JasperUtil.compilaJRXML(arquivoSubreport.getArquivoOriginal());

            parametros.put(arquivoSubreport.getNomeParametro(), new ByteArrayInputStream(templateSubrelatorioCompilado));
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStreamRelatorio, parametros, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        } catch (JRException e) {
            e.printStackTrace();
            throw new FalhaAoGerarRelatorioException();
        }

        String nomeRelatorio = relatorio.getNome() + "-v-" + versaoRelatorio.getNumeroVersao() + ".pdf";

        return new PdfGerado(nomeRelatorio, out.toByteArray());
    }

}
