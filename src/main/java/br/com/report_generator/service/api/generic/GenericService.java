package br.com.report_generator.service.api.generic;

import java.util.List;

public interface GenericService <E, I> {
    public E save(E entity);
    public E update(E entity);
    public void delete(E entity);
    public E findById(Object id);
    public List<E> findAll();
}
