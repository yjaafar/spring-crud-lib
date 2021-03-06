package io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.factory.abs;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.test.controller.ControllerIntegrationTest;
import io.github.vincemann.generic.crud.lib.test.TestConfigurationFactory;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.abs.ControllerTestConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class AbstractControllerTestConfigurationFactory
                <E extends IdentifiableEntity<Id>,
                Id extends Serializable, SuccessfulC extends ControllerTestConfiguration<Id>,
                FailedC extends ControllerTestConfiguration<Id>>

                    implements TestConfigurationFactory<SuccessfulC,FailedC,ControllerTestConfiguration<Id>,ControllerTestConfiguration<Id>>
{
    private ControllerIntegrationTest<E,Id> context;
}
