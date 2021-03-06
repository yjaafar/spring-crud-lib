package io.github.vincemann.generic.crud.lib.test.controller;

import io.github.vincemann.generic.crud.lib.controller.dtoMapper.exception.EntityMappingException;
import io.github.vincemann.generic.crud.lib.controller.springAdapter.SpringAdapterDtoCrudController;
import io.github.vincemann.generic.crud.lib.controller.springAdapter.idFetchingStrategy.UrlParamIdFetchingStrategy;
import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.*;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.factory.*;
import io.github.vincemann.generic.crud.lib.test.controller.requestEntityFactory.RequestEntityFactory;
import io.github.vincemann.generic.crud.lib.test.controller.requestEntityFactory.UrlParamIdRequestEntityFactory;
import io.github.vincemann.generic.crud.lib.test.testExecutionListeners.ResetDatabaseTestExecutionListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Integration Test for a {@link SpringAdapterDtoCrudController} with {@link UrlParamIdFetchingStrategy}, that tests typical Crud operations
 * <p>
 *
 * @param <E>
 * @param <Id>
 */
@Slf4j
//clear database after each test
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {ResetDatabaseTestExecutionListener.class}
)
@Getter
@Setter
public abstract class UrlParamIdControllerIntegrationTest<E extends IdentifiableEntity<Id>, Id extends Serializable>
        extends EagerFetchControllerIntegrationTest<E,Id>
{

    //todo we probably at some point need interfaces before these
    private CreateControllerTest<E,Id> createControllerTest;
    private FindControllerTest<E,Id> findControllerTest;
    private UpdateControllerTest<E,Id> updateControllerTest;
    private DeleteControllerTest<E,Id> deleteControllerTest;
    private FindAllControllerTest<E,Id> findAllControllerTest;

    public UrlParamIdControllerIntegrationTest(String url) {
        super(url);
    }

    public UrlParamIdControllerIntegrationTest() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assertions.assertTrue(UrlParamIdFetchingStrategy.class.isAssignableFrom(getController().getIdIdFetchingStrategy().getClass()));
        initTestsWithDefaults();
    }

    private void initTestsWithDefaults(){
        DeleteControllerTestConfigurationFactory<E,Id> deleteTestConfigFactory = new DeleteControllerTestConfigurationFactory<>(this);
        this.deleteControllerTest = new DeleteControllerTest<E,Id>(this,deleteTestConfigFactory);

        FindControllerTestConfigurationFactory<E,Id> findControllerTestConfigurationFactory = new FindControllerTestConfigurationFactory<>(this);
        this.findControllerTest = new FindControllerTest<>(this,findControllerTestConfigurationFactory);

        UpdateControllerTestConfigurationFactory<E,Id> updateControllerTestConfigurationFactory = new UpdateControllerTestConfigurationFactory<>(this);
        this.updateControllerTest = new UpdateControllerTest<>(this,updateControllerTestConfigurationFactory);

        CreateTestConfigurationFactory<E,Id> createTestConfigurationFactory = new CreateTestConfigurationFactory<>(this);
        this.createControllerTest = new CreateControllerTest<>(this,createTestConfigurationFactory);

        FindAllControllerTestConfigurationFactory<E,Id> findAllControllerTestConfigurationFactory = new FindAllControllerTestConfigurationFactory<>(this);
        this.findAllControllerTest = new FindAllControllerTest<>(this,findAllControllerTestConfigurationFactory);
    }

    @Autowired
    public void injectCrudController(SpringAdapterDtoCrudController<E, Id> crudController) {
        setController(crudController);
    }


    @Override
    protected RequestEntityFactory<Id> provideRequestEntityFactory() {
        return UrlParamIdRequestEntityFactory.<Id>builder()
                .controller(getController())
                .entityIdParamKey(((UrlParamIdFetchingStrategy<Id>) getController().getIdIdFetchingStrategy()).getIdUrlParamKey())
                .baseAddressProvider(this)
                .build();
    }

    protected <Dto extends IdentifiableEntity<Id>> Dto mapEntityToDto(E entity, Class<Dto> dtoClass) throws EntityMappingException {
        return getController().findMapperAndMapToDto(entity, dtoClass);
    }

    protected E mapDtoToEntity(IdentifiableEntity<Id> dto, Class<? extends IdentifiableEntity<Id>> dtoClass) throws EntityMappingException {
        return getController().findMapperAndMapToEntity(dto, dtoClass);
    }


    protected E saveServiceEntity(E e) throws BadEntityException {
        return getTestService().save(e);
    }

    protected Collection<E> saveServiceEntities(Collection<E> ECollection) throws BadEntityException {
        Collection<E> savedEntities = new ArrayList<>();
        for (E e : ECollection) {
            E savedEntity = saveServiceEntity(e);
            savedEntities.add(savedEntity);
        }
        return savedEntities;
    }
}