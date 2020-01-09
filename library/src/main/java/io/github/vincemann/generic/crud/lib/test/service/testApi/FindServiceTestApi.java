package io.github.vincemann.generic.crud.lib.test.service.testApi;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import io.github.vincemann.generic.crud.lib.test.service.testApi.abs.AbstractServiceTestApi;
import io.github.vincemann.generic.crud.lib.test.service.RootServiceTestContext;
import org.junit.jupiter.api.Assertions;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

public class FindServiceTestApi<E extends IdentifiableEntity<Id>, Id extends Serializable,R extends CrudRepository<E,Id>>
        extends AbstractServiceTestApi<E,Id,R> {

    public FindServiceTestApi(RootServiceTestContext<E, Id, R> serviceTestContext) {
        super(serviceTestContext);
    }

    public E findEntityById_ShouldSucceed(Id id) throws NoIdException {
        Assertions.assertNotNull(id);
        Assertions.assertTrue(getRootContext().repoFindById(id).isPresent());
        Optional<E> foundEntity = getRootContext().serviceFindById(id);
        Assertions.assertTrue(foundEntity.isPresent());
        return foundEntity.get();
    }

    public <T extends Throwable> T findExistingEntityById_ShouldFail(Id id, Class<? extends T> expectedException) throws NoIdException {
        Assertions.assertNotNull(id);
        Assertions.assertTrue(getRootContext().repoFindById(id).isPresent());
        return Assertions.assertThrows(expectedException,() -> getRootContext().serviceFindById(id));
    }

    public void findExistingEntityById_ShouldFail(Id id) throws NoIdException {
        Assertions.assertNotNull(id);
        Assertions.assertTrue(getRootContext().repoFindById(id).isPresent());
        Optional<E> foundEntity = getRootContext().serviceFindById(id);
        Assertions.assertFalse(foundEntity.isPresent());
    }
}