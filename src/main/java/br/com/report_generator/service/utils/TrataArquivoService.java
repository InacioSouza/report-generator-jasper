package br.com.report_generator.service.utils;

import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;
import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoInvalidoException;
import br.com.report_generator.infra.exception.IdentificadorArquivoPrincipalInvalidoException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service("br.com.report_generator.service.utils.TrataArquivoService")
public class TrataArquivoService {

    /**
     * Verifica se o arquivo dentro do MultipartFile é um zip,
     * faz validações nos arquivos do zip para garantir o padrão de nomenclatura e conteúdo desejado
     * e retorna arquivos extraídos.
     * @param arquivo MultipartFile
     * @return Map
     */
    public Map<String, byte[]> validaEDevolveArquivosDoZip(MultipartFile arquivo) {

        if(arquivo.isEmpty()) {
            throw new FormatoInvalidoException("O arquivo enviado está vazio!");
        }

        if(arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename().isEmpty()) {
            throw new FormatoInvalidoException("O arquivo enviado não possui nome!");
        }

        if(!ZipUtil.assinaturaDoArquivoCorrespondeZIP(arquivo)) {
            throw new FormatoInvalidoException("O arquivo deve ser do tipo zip !");
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

            var nomeArquivoUpper = nomeArquivo.toUpperCase().split("\\.")[0] + ".jrxml";
            if (nomeArquivoUpper.contains(IdentificadorArquivoPrincipalEnum.MAIN.toString())) {
                qtdArquivosMAIN++;
                mapArquivosNomesSubreportSemExtensao.put(nomeArquivoUpper, mapArquivos.get(nomeArquivo));
                continue;
            }

            //NOTE: Para arquivos de Subreport o nome da extenxão não deve ser considerado no nome do parâmetro
            String nomeParametro = nomeArquivo.split("\\.")[0].toUpperCase();
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
}
