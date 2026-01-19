package br.com.report_generator.service.api;

import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.dto.relatorio.InfoRelatorioResponseDto;
import br.com.report_generator.dto.relatorio.RelatorioCadastradoResponseDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Service para operações relacionadas ao Relatorio.
 * Os métodos de geração do relatório final em PDF não ficam aqui,
 * esse serviço é mais direcionado aos metadados do relatório
 */
public interface RelatorioService extends GenericService<Relatorio, UUID> {

    /**
     * Cadastra o Relatorio e a sua primeira versão (VersaoRelatorio)
     * @param arquivo MultipartFile
     * @param relatorioUploadDto CadastraRelatorioRequestDto
     * @return RelatorioCadastradoResponseDto
     */
    RelatorioCadastradoResponseDto uploadRelatorio(
            MultipartFile arquivo,
            CadastraRelatorioRequestDto relatorioUploadDto,
            Sistema sistemaDoRelatorio);

    /**
     * Busca todas as informações relacionadas ao Relatorio,
     * traz lista de VersaoRelatorio e para estas a respectiva lista de ArquivoSubreport.
     * Não inclui os bytes dos templates (arquivos JRXML).
     * @return InfoRelatorioResponseDto
     */
    List<InfoRelatorioResponseDto> buscaInformacaoDeTodosRelatorios();
}
