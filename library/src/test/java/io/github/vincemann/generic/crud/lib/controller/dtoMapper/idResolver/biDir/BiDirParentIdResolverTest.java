package io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.biDir;

import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.biDir.testEntities.BiDirEntityParent;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.biDir.testEntities.BiDirEntityParentDto;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.exception.EntityMappingException;
import io.github.vincemann.generic.crud.lib.controller.dtoMapper.idResolver.biDir.abs.BiDirEntityResolverTest;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
public class BiDirParentIdResolverTest extends BiDirEntityResolverTest {

    private BiDirParentIdResolver biDirParentIdResolver;

    @BeforeEach
    @Override
    public void setUp() throws NoIdException {
        super.setUp();
        this.biDirParentIdResolver = new BiDirParentIdResolver(getCrudServiceFinder());
    }

    @Test
    public void resolveServiceEntityIds() throws EntityMappingException {
        //given
        BiDirEntityParentDto biDirEntityParentDto = new BiDirEntityParentDto();
        biDirEntityParentDto.setEntityChildId(getBiDirChild().getId());
        BiDirEntityParent unfinishedMappedBiDirEntityParent = new BiDirEntityParent();
        //when
        biDirParentIdResolver.resolveEntityIds(unfinishedMappedBiDirEntityParent,biDirEntityParentDto);
        //then
        Assertions.assertEquals(getBiDirChild(),unfinishedMappedBiDirEntityParent.getBiDIrEntityChild());
    }

    @Test
    public void resolveDtoIds(){
        //given
        BiDirEntityParent entityParent = new BiDirEntityParent();
        entityParent.setBiDIrEntityChild(getBiDirChild());
        BiDirEntityParentDto unfinishedMappedBiDirEntityParentDto = new BiDirEntityParentDto();
        //when
        biDirParentIdResolver.resolveDtoIds(unfinishedMappedBiDirEntityParentDto,entityParent);
        //then
        Assertions.assertEquals(getBiDirChild().getId(), unfinishedMappedBiDirEntityParentDto.getEntityChildId());
    }
}
