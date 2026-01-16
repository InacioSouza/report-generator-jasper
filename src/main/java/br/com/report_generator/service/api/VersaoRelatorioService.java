package br.com.report_generator.service.api;

import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface VersaoRelatorioService extends GenericService<VersaoRelatorio, UUID> {

    VersaoRelatorio buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao);

    VersaoRelatorio buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio);

    VersaoRelatorio cadastraVersaoRelatorio(
            MultipartFile arquivoZip,
            CadastraVersaoRelatorioRequestDto dto,
            Relatorio relatorio
    );

    Integer buscaNumeroVersao(UUID idVersao);

    /**
     * Verifica se o arquivo dentro do MultipartFile é um zip,
     * faz validações nos arquivos do zip para garantir o padrão de nomenclatura e conteúdo desejado
     * e retorna arquivos extraídos.
     * @param arquivo MultipartFile
     * @return Map
     */
    Map<String, byte[]> validaEDevolveArquivosDoZip(MultipartFile arquivo);
}
