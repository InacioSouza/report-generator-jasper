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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Override
    public RelatorioCadastradoResponseDto uploadRelatorio(MultipartFile arquivo, CadastraRelatorioRequestDto relatorioUploadDto) {

        Sistema sistemaEncontrado = this.sistemaService.findById(relatorioUploadDto.idSistema());
        if (sistemaEncontrado == null){
            throw new RegistroNaoEncontradoException(
                    "Não foi encontrado sistema para o id : " + relatorioUploadDto.idSistema()
            );
        }

        Map<String, byte[]> mapArquivos = this.versaoRelatorioService.validaEDevolveArquivosDoZip(arquivo);

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaEncontrado)
                .build();

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

                arquivoSubreport.setNomeParametro(arquivoEntry.getKey());
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
