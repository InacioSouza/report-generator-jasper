package br.com.report_generator.repository;

import br.com.report_generator.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SistemaRepository extends JpaRepository<Sistema, Long> {
}