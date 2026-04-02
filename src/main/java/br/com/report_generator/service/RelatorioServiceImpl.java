package br.com.report_generator.service;

import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;
import br.com.report_generator.dto.filtros.RelatorioFiltroDto;
import br.com.report_generator.dto.relatorio.AtualizaRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.InfoRelatorioResponseDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.infra.exception.AutorizationException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.infra.factor.RelatorioFactor;
import br.com.report_generator.infra.factor.VersaoRelatorioFactor;
import br.com.report_generator.model.*;
import br.com.report_generator.repository.RelatorioRepository;
import br.com.report_generator.repository.specification.RelatorioSpecification;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.JasperUtil;
import br.com.report_generator.service.utils.SecurityUtil;
import br.com.report_generator.service.utils.TrataArquivoService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service("br.com.report_generator.service.RelatorioServiceImpl")
public class RelatorioServiceImpl extends GenericServiceImpl<Relatorio, UUID> implements RelatorioService {

    private final RelatorioRepository repository;
    private final TrataArquivoService trataArquivoService;

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
            Sistema sistemaDoRelatorio,
            Cliente cliente
    ) {

        Map<String, byte[]> mapArquivos = this.trataArquivoService.validaEDevolveArquivosDoZip(arquivo);

        Relatorio relatorio = new RelatorioFactor()
                .constroiRelatorioUtilizandoDto(relatorioUploadDto)
                .addSistema(sistemaDoRelatorio)
                .addCliente(cliente)
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

        return new RelatorioCadastradoResponseDto(this.save(relatorio), versaoRelatorio);
    }

    @Override
    public List<InfoRelatorioResponseDto> buscaRelatorios(RelatorioFiltroDto filtro) {

        Specification<Relatorio> spec = RelatorioSpecification.filtro(filtro);
        List<Relatorio> listRelatorio = this.repository.findAll(spec);

        UUID idCliente = SecurityUtil.buscaIdClienteAutenticado();
        listRelatorio.forEach(relatorio -> {
            if (!relatorio.getCliente().getId()
                    .equals(idCliente)) {
                throw new AutorizationException(
                        "Não é permitido que um cliente busque informações de relatórios que não pertencem a ele!");
            }
        });

        return listRelatorio.stream().map(InfoRelatorioResponseDto::new).toList();
    }

    @Override
    public Integer qtdVersoesParaORelatorio(UUID idRelatorio) {
        return this.repository.qtdVersoesParaORelatorio(idRelatorio);
    }

    @Override
    public void verificaAutorizacaoClienteParaAlterarRelatorio(Relatorio relatorio) {
        UUID idClienteConectado = SecurityUtil.buscaIdClienteAutenticado();

        UUID idClienteDonoRelatorio = relatorio.getCliente().getId();

        if (!idClienteDonoRelatorio.equals(idClienteConectado)) throw new IllegalArgumentException(
                "Um cliente não pode alterar um registro sem ser o dono dele!");
    }

    @Override
    public InfoRelatorioResponseDto atualizarRelatorio(
            UUID idRelatorio,
            AtualizaRelatorioRequestDto dto,
            Sistema sistema
    ) {

        if(!this.repository.existsById(idRelatorio)) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id " + idRelatorio);

        Relatorio relatorio = this.repository.findById(idRelatorio).get();

        if (!relatorio.getCliente().getId()
                .equals(SecurityUtil.buscaIdClienteAutenticado())) {
            throw new AutorizationException(
                    "Não é permitido que um cliente altere um relatórios que não pertence a ele!");
        }

        this.verificaAutorizacaoClienteParaAlterarRelatorio(relatorio);

        relatorio.setTituloPadrao(dto.tituloPadrao());
        relatorio.setSubtituloPadrao(dto.subtituloPadrao());
        relatorio.setNome(dto.nome());
        relatorio.setInformacao(dto.informacao());
        relatorio.setDescricaoTecnica(dto.descricaoTecnica());
        relatorio.setSistema(sistema);
        relatorio.setUltimaAtualizacao(new Date());

        return new InfoRelatorioResponseDto(this.repository.save(relatorio));
    }

    @Override
    public UUID deletarPorId(UUID idRelatorio) {
        this.repository.deleteById(idRelatorio);
        return idRelatorio;
    }
}
