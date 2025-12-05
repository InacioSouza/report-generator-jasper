package br.com.report_generator.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepository<E, I> extends JpaRepository<E, I> {
}
