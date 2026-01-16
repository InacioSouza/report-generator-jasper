package br.com.report_generator.service;

import br.com.report_generator.dto.*;
import br.com.report_generator.dto.relatorio.*;
import br.com.report_generator.infra.exception.*;
import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.infra.factor.VersaoRelatorioFactor;
import br.com.report_generator.model.*;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.SistemaService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.JasperUtil;
import br.com.report_generator.service.utils.ZipUtil;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        if(arquivo.isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado está vazio!");
        }

        if(arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename().isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado não possui nome!");
        }

        if(!ZipUtil.assinaturaDoArquivoCorrespondeZIP(arquivo)) {
            throw new FormatoArquivoInvalidoException("O arquivo deve ser do tipo zip !");
        }

    }

    private Map<String, byte[]> extraiArquivosDoZip(MultipartFile arquivo) {
        Map<String, byte[]> mapArquivos = ZipUtil.extrairArquivosDoZip(arquivo);

        if(mapArquivos.isEmpty()) throw new FalhaAoSalvarRelatorioException("Nenhum arquivo foi extraído do zip");

        int qtdArquivosMAIN = 0;
        for(String nomeArquivo : mapArquivos.keySet().stream().toList()) {

            if (nomeArquivo == null || nomeArquivo.isEmpty()) {
                throw new IllegalArgumentException("Um arquivo presente no zip não possui nome!");
            }

            int qtdPontosNoNomeArquivo = 0;
            for(char c : nomeArquivo.toCharArray()) {
                if (c == '.') {
                    qtdPontosNoNomeArquivo ++;
                }
            }
            if (qtdPontosNoNomeArquivo > 1) throw new IllegalArgumentException(
                    "Nome de arquivo inválido ( " + nomeArquivo + " )" + " possui mais de um caractere '.'"
            );

            if (nomeArquivo.contains(IdentificadorArquivoPrincipalEnum.MAIN.toString())) {
                qtdArquivosMAIN++;
            }
        }

        if (qtdArquivosMAIN > 1) {
            throw new IdentificadorArquivoPrincipalInvalidoException(
                    "Não deve haver mais de 1 arquivo com o trecho " + IdentificadorArquivoPrincipalEnum.MAIN + " no nome!"
            );
        }

        if(mapArquivos.size() > 1 && qtdArquivosMAIN == 0) throw new IdentificadorArquivoPrincipalInvalidoException();

        return mapArquivos;
    }

    @Override
    public RelatorioCadastradoResponseDto uploadRelatorio(MultipartFile arquivo, CadastraRelatorioRequestDto relatorioUploadDto) {

        this.validaArquivoRecebido(arquivo);

        Sistema sistemaEncontrado = this.sistemaService.findById(relatorioUploadDto.idSistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException("Não foi encontrado sistema para o id : " + relatorioUploadDto.idSistema());
        }

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

        Map<String, byte[]> mapArquivos = this.extraiArquivosDoZip(arquivo);

        VersaoRelatorio versaoRelatorio = new VersaoRelatorioFactor()
                .constroiPadraoComDescricaoViaDTO(relatorioUploadDto)
                .build();

        if (mapArquivos.size() == 1) {
            versaoRelatorio.setNomeArquivo(mapArquivos.keySet().stream().findFirst().get());

            Optional<byte[]> jrxml = mapArquivos.values().stream().findFirst();
            versaoRelatorio.setArquivoOriginal(jrxml.get());
            versaoRelatorio.setArquivoCompilado(JasperUtil.compilaJRXML(jrxml.get()));

        } else {

            for(Map.Entry<String, byte[]> arquivoEntry : mapArquivos.entrySet()) {

                if (arquivoEntry.getKey().contains(IdentificadorArquivoPrincipalEnum.MAIN.toString())) {
                    versaoRelatorio.setNomeArquivo(arquivoEntry.getKey());
                    versaoRelatorio.setArquivoOriginal(arquivoEntry.getValue());
                    versaoRelatorio.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));
                    continue;
                }

                ArquivoSubreport arquivoSubreport = new ArquivoSubreport();

                //NOTE: Retira a extensão do arquivo para manter apenas o nome, o qual é equivalente ao parâmetro esperado pelo Jasper
                String nomeParametro = arquivoEntry.getKey().split(".")[0];
                arquivoSubreport.setNomeParametro(nomeParametro);
                arquivoSubreport.setArquivoOriginal(arquivoEntry.getValue());
                arquivoSubreport.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));
                arquivoSubreport.setVersaoRelatorio(versaoRelatorio);

                versaoRelatorio.getListSubreport().add(arquivoSubreport);
            }
        }

        versaoRelatorio.setRelatorio(relatorio);
        versaoRelatorio.setNumeroVersao(1);

        relatorio.getListVersoes().add(versaoRelatorio);

        Relatorio relatorioSalvo = this.save(relatorio);
        versaoRelatorio.setNumeroVersao(this.versaoRelatorioService.buscaNumeroVersao(versaoRelatorio.getId()));

        return new RelatorioCadastradoResponseDto(relatorioSalvo, versaoRelatorio);
    }

    @Override
    public void baixarRelatorio (
            BaixarRelatorioRequestDto dto,
            HttpServletResponse httpResponse
    ) {

        Relatorio relatorio = this.findById(dto.idRelatorio());
        if (relatorio == null) throw new RegistroNaoEncontradoException("Não foi encontrado relatório para o id: " + dto.idRelatorio());

        VersaoRelatorio versaoRelatorio;
        if(dto.numeroVersao() == null) {
            versaoRelatorio = this.versaoRelatorioService.buscaVersaoRelatorioMaisRecentePara(dto.idRelatorio());
        } else {
            versaoRelatorio = this.versaoRelatorioService.buscaVersaoRelatorioPorIdRelatorio(dto.idRelatorio(), dto.numeroVersao());
        }

        if (versaoRelatorio == null) throw new RegistroNaoEncontradoException("Não foi encontrado versão de relatório para o número: " + dto.numeroVersao());

        Map<String, byte[]> mapArquivos = new HashMap<>();
        mapArquivos.put(versaoRelatorio.getNomeArquivo(), versaoRelatorio.getArquivoOriginal());

        List<ArquivoSubreport> listSubreports = this.arquivoSubreportService.buscarSubreportsPorVersao(versaoRelatorio.getId());
        for(ArquivoSubreport arquivoSubreport : listSubreports) {
            mapArquivos.put(arquivoSubreport.getNomeParametro(), arquivoSubreport.getArquivoOriginal());
        }

        byte[] zipArquivos = ZipUtil.gerarZip(mapArquivos);

        try {
            httpResponse.getOutputStream().write(zipArquivos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String nomeZip = relatorio.getNome() + ".zip";

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentType("application/zip");
        httpResponse.setHeader("Content-Disposition", dto.exibicao().getExibicao() + "; filename=\"" + nomeZip + "\"");
    }

    @Override
    public List<InfoRelatorioResponseDto> buscaInformacaoDeTodosRelatorios() {

        return this.findAll()
                .stream()
                .map(InfoRelatorioResponseDto::new)
                .toList();

    }
}
