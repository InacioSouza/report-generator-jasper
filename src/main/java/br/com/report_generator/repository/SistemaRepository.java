package br.com.report_generator.repository;

import br.com.report_generator.model.Sistema;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SistemaRepository extends GenericRepository<Sistema, UUID> {
}