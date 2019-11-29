package io.github.vincemann.demo.service.springDataJPA;

import io.github.vincemann.demo.model.Owner;
import io.github.vincemann.demo.repositories.OwnerRepository;
import io.github.vincemann.demo.service.OwnerService;
import io.github.vincemann.demo.service.plugins.AclPlugin;
import io.github.vincemann.demo.service.plugins.SaveNameToWordPressDb_Plugin;
import io.github.vincemann.generic.crud.lib.service.jpa.JPACrudService;
import io.github.vincemann.generic.crud.lib.service.plugin.BiDirParentPlugin;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Profile("springdatajpa")
public class OwnerJPAService extends JPACrudService<Owner,Long,OwnerRepository> implements OwnerService {


    public OwnerJPAService(
            OwnerRepository jpaRepository,
            BiDirParentPlugin<Owner,Long> biDirParentPlugin,
            AclPlugin aclPlugin,
            SaveNameToWordPressDb_Plugin saveNameToWordPressDbPlugin
    )
    {
        super(
                jpaRepository,
                biDirParentPlugin,
                aclPlugin,
                saveNameToWordPressDbPlugin
        );
    }

    @Transactional
    @Override
    public Optional<Owner> findByLastName(String lastName) {
        return getRepository().findByLastName(lastName);
    }

}
