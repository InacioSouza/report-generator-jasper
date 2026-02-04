package br.com.report_generator.service.api.generic;

import br.com.report_generator.repository.generic.GenericRepository;

import java.util.List;

public interface GenericService <E, I> {
    E save(E entity);
    List<E> saveAll(List<E> entities);
    E update(E entity);
    void delete(E entity);
    E findById(Object id);
    List<E> findAll();
    boolean existeRegistroParaId(Object id);
    GenericRepository getRepository();
}
