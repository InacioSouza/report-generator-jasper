package br.com.report_generator.service;

import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.infra.factor.VersaoRelatorioFactor;
import br.com.report_generator.model.*;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.service.api.ArquivoSubreportService;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.InfoRelatorioResponseDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.JasperUtil;
import br.com.report_generator.service.utils.TrataArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service("br.com.report_generator.service.RelatorioServiceImpl")
public class RelatorioServiceImpl extends GenericServiceImpl<Relatorio, UUID> implements RelatorioService {

    private final RelatorioRepository repository;
    private final TrataArquivoService trataArquivoService;

    @Autowired
    private ArquivoSubreportService arquivoSubreportService;

    RelatorioServiceImpl(
            RelatorioRepository repository,
            TrataArquivoService trataArquivoService
    ) {
        super(repository);
        this.repository = repository;
        this.trataArquivoService = trataArquivoService;
    }

    @Override
    public RelatorioCadastradoResponseDto uploadRelatorio(
            MultipartFile arquivo,
            CadastraRelatorioRequestDto relatorioUploadDto,
            Sistema sistemaDoRelatorio
    ) {

        Map<String, byte[]> mapArquivos = this.trataArquivoService.validaEDevolveArquivosDoZip(arquivo);

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaDoRelatorio)
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
        relatorio.setNumeroUltimaVersao(1);

        relatorio.getListVersoes().add(versaoRelatorio);

        Relatorio relatorioSalvo = this.save(relatorio);

        return new RelatorioCadastradoResponseDto(relatorioSalvo, versaoRelatorio);
    }

    @Override
    public List<InfoRelatorioResponseDto> buscaInformacaoDeTodosRelatorios() {

        return this.findAll()
                .stream()
                .map(InfoRelatorioResponseDto::new)
                .toList();

    }
}
