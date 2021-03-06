package io.github.vincemann.generic.crud.lib.test.controller.crudTests;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.test.controller.ControllerIntegrationTest;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.abs.AbstractControllerTest;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.abs.ControllerTestConfiguration;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.factory.abs.AbstractControllerTestConfigurationFactory;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FindAllControllerTest<E extends IdentifiableEntity<Id>, Id extends Serializable>
        extends AbstractControllerTest<E,Id>
{


    private AbstractControllerTestConfigurationFactory<E, Id, ControllerTestConfiguration<Id>, ControllerTestConfiguration<Id>> configFactory;

    public FindAllControllerTest(ControllerIntegrationTest<E, Id> testContext, AbstractControllerTestConfigurationFactory<E, Id, ControllerTestConfiguration<Id>, ControllerTestConfiguration<Id>> configFactory) {
        super(testContext);
        this.configFactory = configFactory;
    }


    public <Dto extends IdentifiableEntity<Id>> Set<Dto> findAllEntities_ShouldSucceed() throws Exception {
        return findAllEntities_ShouldSucceed(configFactory.createDefaultSuccessfulConfig());
    }


    public <Dto extends IdentifiableEntity<Id>> Set<Dto> findAllEntities_ShouldSucceed(ControllerTestConfiguration<Id>... modifications) throws Exception {
        ControllerTestConfiguration<Id> config = configFactory.createMergedSuccessfulConfig(modifications);
        ResponseEntity<String> responseEntity = findAllEntities(config);

        @SuppressWarnings("unchecked")
        Set<IdentifiableEntity<Id>> httpResponseDtos = getTestContext().getController().getMediaTypeStrategy().readDtosFromBody(responseEntity.getBody(), mappingContext().getFindAllReturnDtoClass(), Set.class);
        //check if dto type is correct
        List<Id> idsSeen = new ArrayList<>();
        for (IdentifiableEntity<Id> dto : httpResponseDtos) {
            Assertions.assertEquals(mappingContext().getFindAllReturnDtoClass(),dto.getClass());
            //prevent duplicates
            Assertions.assertFalse(idsSeen.contains(dto.getId()));
            idsSeen.add(dto.getId());
        }
        return (Set<Dto>) httpResponseDtos;
    }



    public ResponseEntity<String> findAllEntities_ShouldFail() throws Exception {
        return findAllEntities_ShouldFail(configFactory.createDefaultFailedConfig());
    }


    public ResponseEntity<String> findAllEntities_ShouldFail(ControllerTestConfiguration<Id>... modifications) throws Exception {
        ControllerTestConfiguration<Id> config = configFactory.createMergedFailedConfig(modifications);
        return findAllEntities(config);
    }

    public ResponseEntity<String> findAllEntities(ControllerTestConfiguration<Id> config) {
        return sendRequest(getTestContext().getRequestEntityFactory().create(config,null,null, ControllerTestMethod.FIND_ALL));
    }
}
