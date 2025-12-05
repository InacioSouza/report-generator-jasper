package br.com.report_generator.repository;

import br.com.report_generator.model.Template;
import br.com.report_generator.repository.generic.GenericRepository;

import java.util.UUID;

public interface TemplateRelatorioRepository extends GenericRepository<Template, UUID> {
}