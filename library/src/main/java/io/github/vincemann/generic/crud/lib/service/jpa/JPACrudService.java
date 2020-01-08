package io.github.vincemann.generic.crud.lib.service.jpa;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.service.CrudService;
import io.github.vincemann.generic.crud.lib.service.plugin.CrudService_PluginProxy;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.service.exception.EntityNotFoundException;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import io.github.vincemann.generic.crud.lib.util.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * PluginCode from {@link CrudService_PluginProxy.Plugin}s runs in the Transaction of the ServiceMethod.
 * @param <E>
 * @param <Id>
 * @param <R>
 */
public abstract class JPACrudService
                <
                          E extends IdentifiableEntity<Id>,
                          Id extends Serializable,
                          R extends JpaRepository<E,Id>
                >
        implements CrudService<E,Id,R> {


    private R jpaRepository;
    @SuppressWarnings("unchecked")
    private Class<E> entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JPACrudService() {
    }

    @Autowired
    public void injectJpaRepository(R jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Transactional
    @Override
    public Optional<E> findById(Id id) throws NoIdException {
        if(id==null){
            throw new NoIdException("No Id value set for EntityType: " + entityClass.getSimpleName());
        }
        return jpaRepository.findById(id);
    }



    @Transactional
    @Override
    public E update(E update) throws EntityNotFoundException, NoIdException, BadEntityException {
        try {
            if(update.getId()==null){
                throw new NoIdException("No Id value set for EntityType: " + entityClass.getSimpleName());
            }

            Optional<E> entityToUpdate = findById(update.getId());
            if(!entityToUpdate.isPresent()){
                throw new EntityNotFoundException(update.getId(), entityClass);
            }
            //copy non null values from update to entityToUpdate
            BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
            notNull.copyProperties(entityToUpdate.get(), update);
            return save(entityToUpdate.get());
        }catch (IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @Override
    public E save(E entity) throws BadEntityException {
        try {
            return jpaRepository.save(entity);
        }
        catch (NonTransientDataAccessException e){
            throw new BadEntityException(e);
        }
    }

    @Transactional
    @Override
    public Set<E> findAll() {
        return new HashSet<>(jpaRepository.findAll());
    }

    @Transactional
    @Override
    public void delete(E entity) throws EntityNotFoundException, NoIdException {
        if(entity.getId()==null){
            throw new NoIdException("No Id value set for EntityType: " + entityClass.getSimpleName());
        }
        if(!findById(entity.getId()).isPresent()){
            throw new EntityNotFoundException(entity.getId(),entity.getClass());
        }
        jpaRepository.delete(entity);
    }

    @Transactional
    @Override
    public void deleteById(Id id) throws EntityNotFoundException, NoIdException {
        if(id==null){
            throw new NoIdException("No Id value set for EntityType: " + entityClass.getSimpleName());
        }
        Optional<E> entity = findById(id);
        if(!entity.isPresent()){
            throw new EntityNotFoundException(id, entityClass);
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public R getRepository() {
        return jpaRepository;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }
}
