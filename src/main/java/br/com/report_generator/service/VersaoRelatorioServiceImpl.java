package br.com.report_generator.service;

import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service("br.com.report_generator.service.1 VersaoRelatorioServiceImpl")
public class VersaoRelatorioServiceImpl extends GenericServiceImpl<VersaoRelatorio, UUID> implements VersaoRelatorioService {

    private final VersaoRelatorioRepository repository;

    public VersaoRelatorioServiceImpl(VersaoRelatorioRepository repository) {
        super(repository);
        this.repository = repository;
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
    public VersaoRelatorio cadastraVersaoRelatorio(MultipartFile arquivoZip, CadastraVersaoRelatorioRequestDto dto) {



        return null;
    }

    @Override
    public Integer buscaNumeroVersao(UUID idVersao) {
        return this.repository.buscaNumeroVersao(idVersao);
    }

}
