package io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir;

import io.github.vincemann.generic.crud.lib.controller.dtoMapper.exception.EntityMappingException;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.abs.UniDirEntityResolverTest;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.testEntities.UniDirEntityChild;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.testEntities.UniDirEntityChildDto;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UniDirChildIdResolverTest extends UniDirEntityResolverTest {

    private UniDirChildIdResolver uniDirChildIdResolver;

    @BeforeEach
    @Override
    public void setUp() throws NoIdException {
        super.setUp();
        this.uniDirChildIdResolver = new UniDirChildIdResolver(getCrudServiceFinder());
    }

    @Test
    public void resolveServiceEntityIds() throws EntityMappingException {
        //given
        UniDirEntityChild unfinishedMappedUniDirChild = new UniDirEntityChild();
        UniDirEntityChildDto childDto = new UniDirEntityChildDto();
        childDto.setParentId(getUniDirEntityChildsParent().getId());
        //when
        uniDirChildIdResolver.resolveEntityIds(unfinishedMappedUniDirChild,childDto);
        //then
        Assertions.assertEquals(getUniDirEntityChildsParent(),unfinishedMappedUniDirChild.getUniDirEntityChildsParent());
    }

    @Test
    void resolveDtoIds() {
        //given
        UniDirEntityChildDto unfinishedMappedUniDirChildDto = new UniDirEntityChildDto();
        UniDirEntityChild child = new UniDirEntityChild();
        child.setUniDirEntityChildsParent(getUniDirEntityChildsParent());
        //when
        uniDirChildIdResolver.resolveDtoIds(unfinishedMappedUniDirChildDto,child);
        //then
        Assertions.assertEquals(getUniDirEntityChildsParent().getId(),unfinishedMappedUniDirChildDto.getParentId());
    }
}
