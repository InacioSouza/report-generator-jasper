package br.com.report_generator.repository;

import br.com.report_generator.model.Relatorio;
import br.com.report_generator.repository.generic.GenericRepository;

import java.util.UUID;

public interface RelatorioRepository extends GenericRepository<Relatorio, UUID> {
}