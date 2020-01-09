package io.github.vincemann.generic.crud.lib.test.service;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.service.CrudService;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.service.exception.EntityNotFoundException;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import io.github.vincemann.generic.crud.lib.test.equalChecker.EqualChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class RootServiceTestContext<E extends IdentifiableEntity<Id>, Id extends Serializable,R extends CrudRepository<E,Id>> {

    private CrudService<E,Id,R> crudService;
    private R repository;
    private EqualChecker<E> defaultEqualChecker;


    public E repoSave(E entityToSave){
        return getRepository().save(entityToSave);
    }
    public Optional<E> repoFindById(Id id){
        return getRepository().findById(id);
    }
    public E serviceSave(E entity) throws BadEntityException {
        return getCrudService().save(entity);
    }
    public Optional<E> serviceFindById(Id id) throws NoIdException {
        return getCrudService().findById(id);
    }
    public E serviceUpdate(E entity,boolean full) throws EntityNotFoundException, BadEntityException, NoIdException {
        return getCrudService().update(entity,full);
    }
    public <S extends CrudService<E,Id,R>> S getCastedCrudService(){
        return (S) crudService;
    }
}