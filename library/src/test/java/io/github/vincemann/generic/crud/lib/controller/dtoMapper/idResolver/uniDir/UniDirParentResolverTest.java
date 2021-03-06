package io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir;

import io.github.vincemann.generic.crud.lib.controller.dtoMapper.exception.EntityMappingException;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.abs.UniDirEntityResolverTest;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.testEntities.UniDirEntityParent;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.uniDir.testEntities.UniDirEntityParentDto;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UniDirParentResolverTest extends UniDirEntityResolverTest {

    private UniDirParentIdResolver uniDirParentIdResolver;

    @BeforeEach
    @Override
    public void setUp() throws NoIdException {
        super.setUp();
        this.uniDirParentIdResolver = new UniDirParentIdResolver(getCrudServiceFinder());
    }

    @Test
    public void resolveServiceEntityIds() throws EntityMappingException {
        //given
        UniDirEntityParent unfinishedMappedUniDirParent = new UniDirEntityParent();
        UniDirEntityParentDto parentDto = new UniDirEntityParentDto();
        parentDto.setChildId(getUniDirEntityParentsChild().getId());
        //when
        uniDirParentIdResolver.resolveEntityIds(unfinishedMappedUniDirParent,parentDto);
        //then
        Assertions.assertEquals(getUniDirEntityParentsChild(),unfinishedMappedUniDirParent.getEntityChild());
    }

    @Test
    void resolveDtoIds() {
        //given
        UniDirEntityParentDto unfinishedMappedUniDirParentDto = new UniDirEntityParentDto();
        UniDirEntityParent parent = new UniDirEntityParent();
        parent.setEntityChild(getUniDirEntityParentsChild());
        //when
        uniDirParentIdResolver.resolveDtoIds(unfinishedMappedUniDirParentDto,parent);
        //then
        Assertions.assertEquals(getUniDirEntityParentsChild().getId(),unfinishedMappedUniDirParentDto.getChildId());
    }
}
