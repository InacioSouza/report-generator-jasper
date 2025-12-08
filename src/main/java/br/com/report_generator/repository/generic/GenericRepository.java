package br.com.report_generator.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<E, I> extends JpaRepository<E, I> {
}
