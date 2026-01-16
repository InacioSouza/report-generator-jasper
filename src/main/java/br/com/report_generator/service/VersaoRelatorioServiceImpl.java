package br.com.report_generator.service;

import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;
import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.IdentificadorArquivoPrincipalInvalidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.service.generic.GenericServiceImpl;
import br.com.report_generator.service.utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service("br.com.report_generator.service.1 VersaoRelatorioServiceImpl")
public class VersaoRelatorioServiceImpl extends GenericServiceImpl<VersaoRelatorio, UUID> implements VersaoRelatorioService {

    @Autowired
    private RelatorioService relatorioService;

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
    public Map<String, byte[]> validaEDevolveArquivosDoZip(MultipartFile arquivo) {

        if(arquivo.isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado está vazio!");
        }

        if(arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename().isEmpty()) {
            throw new FormatoArquivoInvalidoException("O arquivo enviado não possui nome!");
        }

        if(!ZipUtil.assinaturaDoArquivoCorrespondeZIP(arquivo)) {
            throw new FormatoArquivoInvalidoException("O arquivo deve ser do tipo zip !");
        }

        Map<String, byte[]> mapArquivos = ZipUtil.extrairArquivosDoZip(arquivo);

        if(mapArquivos.isEmpty()) throw new FalhaAoSalvarRelatorioException("Nenhum arquivo foi extraído do zip");

        long qtdArquivosComExtensaoCorreta = mapArquivos.keySet()
                .stream().filter(chave -> chave.endsWith(".jrxml"))
                .count();
        if (qtdArquivosComExtensaoCorreta != mapArquivos.size()) throw new IllegalArgumentException(
                "Todos os arquivos dentro do zip devem ter a extensão .jrxml!"
        );

        Map<String, byte[]> mapArquivosNomesSubreportSemExtensao = new HashMap<>();

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
                mapArquivosNomesSubreportSemExtensao.put(nomeArquivo, mapArquivos.get(nomeArquivo));
                continue;
            }

            //NOTE: Para arquivos de Subreport o nome da extenxão não deve ser considerado no nome do parâmetro
            String nomeParametro = nomeArquivo.split("\\.")[0];
            mapArquivosNomesSubreportSemExtensao.put(nomeParametro, mapArquivos.get(nomeArquivo));
        }

        if (qtdArquivosMAIN > 1) {
            throw new IdentificadorArquivoPrincipalInvalidoException(
                    "Não deve haver mais de 1 arquivo com o trecho " + IdentificadorArquivoPrincipalEnum.MAIN + " no nome!"
            );
        }

        if(mapArquivos.size() > 1 && qtdArquivosMAIN == 0) throw new IdentificadorArquivoPrincipalInvalidoException();

        return mapArquivosNomesSubreportSemExtensao;
    }

    @Override
    public VersaoRelatorio cadastraVersaoRelatorio(MultipartFile arquivoZip, CadastraVersaoRelatorioRequestDto dto) {

        Relatorio relatorio = this.relatorioService.findById(dto.idRelatorio());
        if (relatorio == null) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id: " + dto.idRelatorio()
        );

        VersaoRelatorio novaVersaoRelatorio = new VersaoRelatorio();
        novaVersaoRelatorio.setDescricaoVersao(dto.descricaoVersao());
        novaVersaoRelatorio.setRelatorio(relatorio);





        return null;
    }

    @Override
    public Integer buscaNumeroVersao(UUID idVersao) {
        return this.repository.buscaNumeroVersao(idVersao);
    }

}
