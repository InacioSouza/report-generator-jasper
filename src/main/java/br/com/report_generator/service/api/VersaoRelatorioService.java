package br.com.report_generator.service.api;

import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.UUID;

public interface VersaoRelatorioService extends GenericService<VersaoRelatorio, UUID> {
    VersaoRelatorio buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao);
    VersaoRelatorio buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio);
}
