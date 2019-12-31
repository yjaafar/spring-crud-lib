package io.github.vincemann.demo.service.plugin;

import io.github.vincemann.demo.model.abs.Person;
import io.github.vincemann.generic.crud.lib.controller.springAdapter.DtoCrudController_SpringAdapter;
import io.github.vincemann.generic.crud.lib.service.plugin.CrudService_PluginProxy;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *Plugin that can be plugged in to all {@link DtoCrudController_SpringAdapter} dealing with Person Objects
 */
@Slf4j
@Component
public class SaveNameToWordPressDb_Plugin extends CrudService_PluginProxy.Plugin<Person,Long> {

    @Override
    public void onBeforeSave(Person entity) throws BadEntityException {
        super.onBeforeSave(entity);
        log.debug("saving Persons name: "+ entity.getFirstName() + " into wordpress database");
        /* save name of every created Person in seperate db table for example*/
    }
}
