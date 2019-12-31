package io.github.vincemann.generic.crud.lib.service.plugin;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.service.sessionReattach.EntityGraph_SessionReattachment_Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SessionReattachmentPlugin
    //todo why doesnt this work with Serializable instead of Long?
        extends CrudService_PluginProxy.Plugin<IdentifiableEntity<Long>,Long> {

    private EntityGraph_SessionReattachment_Helper entityGraph_sessionReattachment_helper;

    @Autowired
    public SessionReattachmentPlugin(EntityGraph_SessionReattachment_Helper entityGraph_sessionReattachment_helper) {
        this.entityGraph_sessionReattachment_helper = entityGraph_sessionReattachment_helper;
    }

    @Transactional
    @Override
    public void onBeforeSave(IdentifiableEntity entity) throws BadEntityException {
        entityGraph_sessionReattachment_helper.attachEntityGraphToCurrentSession(entity);
        super.onBeforeSave(entity);
    }


}
