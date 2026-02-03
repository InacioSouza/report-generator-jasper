package br.com.report_generator.service.api;

import br.com.report_generator.dto.filtros.RelatorioFiltroDto;
import br.com.report_generator.dto.relatorio.AtualizaRelatorioRequestDto;
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
     * Busca registros de Relatorio de acordo com os campos preenchidos no filtro,
     * caso não haja nenhum campo preenchido busca todos os registros.
     * Retorna uma lista com as informações dos relatórios,
     * não inclui os bytes dos templates (arquivos JRXML).
     * @return InfoRelatorioResponseDto
     */
    List<InfoRelatorioResponseDto> buscaRelatorios(RelatorioFiltroDto filtro);

    Integer qtdVersoesParaORelatorio(UUID idRelatorio);

    InfoRelatorioResponseDto atualizarRelatorio(AtualizaRelatorioRequestDto dto,Sistema sistema);

    UUID deletarPorId(UUID idRelatorio);
}
