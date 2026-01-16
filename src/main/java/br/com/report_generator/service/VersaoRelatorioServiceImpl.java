package br.com.report_generator.service;

import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;
import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.IdentificadorArquivoPrincipalInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.ArquivoSubreport;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.JasperUtil;
import br.com.report_generator.service.utils.TrataArquivoService;
import br.com.report_generator.service.utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service("br.com.report_generator.service.VersaoRelatorioServiceImpl")
public class VersaoRelatorioServiceImpl extends GenericServiceImpl<VersaoRelatorio, UUID> implements VersaoRelatorioService {

    private final VersaoRelatorioRepository repository;
    private final TrataArquivoService trataArquivoService;

    public VersaoRelatorioServiceImpl(
            VersaoRelatorioRepository repository,
            TrataArquivoService trataArquivoService) {
        super(repository);
        this.repository = repository;
        this.trataArquivoService = trataArquivoService;
    }

    @Override
    public VersaoRelatorio buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao) {
        Optional<VersaoRelatorio> versaoRelatorioOptional = this.repository.buscaVersaoRelatorioPorIdRelatorio(idRelatorio, numeroVersao);
        return versaoRelatorioOptional.orElse(null);
    }

    @Override
    public VersaoRelatorio buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio) {
        Optional<VersaoRelatorio> versaoRelatorioOptional = this.repository.buscaVersaoRelatorioMaisRecentePara(idRelatorio);
        return versaoRelatorioOptional.orElse(null);
    }

    @Override
    public VersaoRelatorio cadastraVersaoRelatorio(MultipartFile arquivoZip,
                                                   CadastraVersaoRelatorioRequestDto dto,
                                                   Relatorio relatorio) {

        VersaoRelatorio novaVersaoRelatorio = new VersaoRelatorio();
        novaVersaoRelatorio.setDescricaoVersao(dto.descricaoVersao());
        novaVersaoRelatorio.setRelatorio(relatorio);
        novaVersaoRelatorio.setListSubreport(new ArrayList<>());

        Map<String, byte[]> mapArquivos = this.trataArquivoService.validaEDevolveArquivosDoZip(arquivoZip);

        for(Map.Entry<String, byte[]> arquivoEntry : mapArquivos.entrySet()) {

            if (arquivoEntry.getKey().contains(IdentificadorArquivoPrincipalEnum.MAIN.toString())) {
                novaVersaoRelatorio.setNomeArquivo(arquivoEntry.getKey());
                novaVersaoRelatorio.setArquivoOriginal(arquivoEntry.getValue());
                novaVersaoRelatorio.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));

                if (relatorio.getNumeroUltimaVersao() != null) {
                    novaVersaoRelatorio.setNumeroVersao(relatorio.getNumeroUltimaVersao() + 1);
                } else {
                    // Registros de VersaoRelatorio com numéro de versão negativo não possuem número de versão válido definido
                    novaVersaoRelatorio.setNumeroVersao(-1);
                }
                continue;
            }

            ArquivoSubreport arquivoSubreport = new ArquivoSubreport();

            arquivoSubreport.setNomeParametro(arquivoEntry.getKey());
            arquivoSubreport.setArquivoOriginal(arquivoEntry.getValue());
            arquivoSubreport.setArquivoCompilado(JasperUtil.compilaJRXML(arquivoEntry.getValue()));

            arquivoSubreport.setVersaoRelatorio(novaVersaoRelatorio);

            novaVersaoRelatorio.getListSubreport().add(arquivoSubreport);
        }

        relatorio.setNumeroUltimaVersao(novaVersaoRelatorio.getNumeroVersao());
        return this.save(novaVersaoRelatorio);
    }

    @Override
    public Integer buscaNumeroVersao(UUID idVersao) {
        return this.repository.buscaNumeroVersao(idVersao);
    }

}
