package br.com.report_generator.repository;

import br.com.report_generator.model.Relatorio;
import br.com.report_generator.repository.generic.GenericRepository;
import br.com.report_generator.shared.Teste;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RelatorioRepository extends GenericRepository<Relatorio, UUID> {
}