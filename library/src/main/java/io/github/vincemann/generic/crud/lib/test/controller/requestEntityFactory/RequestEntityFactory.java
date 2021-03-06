package io.github.vincemann.generic.crud.lib.test.controller.requestEntityFactory;

import io.github.vincemann.generic.crud.lib.test.controller.crudTests.ControllerTestMethod;
import io.github.vincemann.generic.crud.lib.test.controller.crudTests.config.abs.ControllerTestConfiguration;
import io.github.vincemann.generic.crud.lib.test.controller.BaseAddressProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.RequestEntity;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public abstract class RequestEntityFactory<Id extends Serializable> {
    private BaseAddressProvider baseAddressProvider;

    public abstract RequestEntity<?> create(ControllerTestConfiguration<Id> config, Object body,Id id, ControllerTestMethod controllerTestMethod);

}
