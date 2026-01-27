package br.com.report_generator.service.api;

import br.com.report_generator.dto.versaorelatorio.AtualizaVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.InfoVersaoRelatorioResponseDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioResponseDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VersaoRelatorioService extends GenericService<VersaoRelatorio, UUID> {

    VersaoRelatorio buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao);

    VersaoRelatorio buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio);

    /**
     * Cadastra uma nova versão para um relatório existente,
     * atualiza o atributo numeroUltimaVersao de Relatorio
     * para o número da versão cadastrada.
     * @param arquivoZip MultipartFile
     * @param dto CadastraVersaoRelatorioRequestDto
     * @param relatorio Relatorio
     * @return VersaoRelatorioResponseDto
     */
    VersaoRelatorioResponseDto cadastraVersaoRelatorio(
            MultipartFile arquivoZip,
            CadastraVersaoRelatorioRequestDto dto,
            Relatorio relatorio
    );

    InfoVersaoRelatorioResponseDto atualizar(AtualizaVersaoRelatorioRequestDto dto);
}
