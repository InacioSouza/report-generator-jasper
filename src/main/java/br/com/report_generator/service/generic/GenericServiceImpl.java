package br.com.report_generator.service.generic;

import br.com.report_generator.repository.generic.GenericRepository;
import br.com.report_generator.service.api.generic.GenericService;

import java.util.List;
import java.util.Optional;

public class GenericServiceImpl<E, I> implements GenericService<E, I> {

    private final GenericRepository<E, I> repository;

    public GenericServiceImpl(GenericRepository<E, I> repository) {
        this.repository = repository;
    }

    @Override
    public E save(E entity) {
        return this.repository.save(entity);
    }

    @Override
    public E update(E entity) {
        return this.repository.save(entity);
    }

    @Override
    public void delete(E entity) {
        this.repository.delete(entity);
    }

    @Override
    public E findById(Object id) {
        Optional<E> objeto = this.repository.findById((I) id);
        return objeto.orElse(null);
    }

    @Override
    public List<E> findAll() {
        return this.repository.findAll();
    }

    @Override
    public boolean existeRegistroParaId(Object id) {
        return this.repository.existsById((I) id);
    }

    @Override
    public GenericRepository getRepository() {
        return this.repository;
    }
}
