package io.github.vincemann.generic.crud.lib.service.plugin;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;
import io.github.vincemann.generic.crud.lib.model.biDir.child.BiDirChild;
import io.github.vincemann.generic.crud.lib.model.biDir.parent.BiDirParent;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.service.exception.EntityNotFoundException;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
/**
 * This plugin, combined with {@link io.github.vincemann.generic.crud.lib.jpaAuditing.entityListener.BiDirChildEntityListener} manages bidirectional relationships of the entity, managed by the service using this plugin.
 * and the parent side must be managed via {@link BiDirParentPlugin} and {@link io.github.vincemann.generic.crud.lib.jpaAuditing.entityListener.BiDirParentEntityListener} as well.
 */
public class BiDirChildPlugin
        <
                E extends IdentifiableEntity<Id> & BiDirChild,
                Id extends Serializable
        >  extends CrudServicePlugin<E,Id> {



    public void onBeforeUpdate(E entity,boolean full) throws EntityNotFoundException, NoIdException, BadEntityException {
        try {
            manageBiDirRelations(entity);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    private void manageBiDirRelations(E newBiDirChild) throws NoIdException, EntityNotFoundException, IllegalAccessException {
        //find already persisted biDirChild (preUpdateState of child)
        Optional<E> oldBiDirChildOptional = getService().findById(newBiDirChild.getId());
        if(!oldBiDirChildOptional.isPresent()){
            throw new EntityNotFoundException(newBiDirChild.getId(),newBiDirChild.getClass());
        }
        E oldBiDirChild = oldBiDirChildOptional.get();
        Collection<BiDirParent> oldParents = oldBiDirChild.findParents();
        Collection<BiDirParent> newParents = newBiDirChild.findParents();
        //find removed parents
        List<BiDirParent> removedParents = new ArrayList<>();
        for (BiDirParent oldParent : oldParents) {
            if(!newParents.contains(oldParent)){
                removedParents.add(oldParent);
            }
        }
        //find added parents
        List<BiDirParent> addedParents = new ArrayList<>();
        for (BiDirParent newParent : newParents) {
            if(!oldParents.contains(newParent)){
                addedParents.add(newParent);
            }
        }

        //dismiss removed Parents Children
        for (BiDirParent removedParent : removedParents) {
            removedParent.dismissChild(newBiDirChild);
        }

        //add added Parent to child
        for (BiDirParent addedParent : addedParents) {
            addedParent.addChild(newBiDirChild);
        }
    }
}
