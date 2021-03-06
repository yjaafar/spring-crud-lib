package io.github.vincemann.demo.service.springDataJPA.it;

import io.github.vincemann.demo.model.Owner;
import io.github.vincemann.demo.model.Pet;
import io.github.vincemann.demo.model.PetType;
import io.github.vincemann.demo.repositories.PetRepository;
import io.github.vincemann.demo.service.OwnerService;
import io.github.vincemann.demo.service.PetTypeService;
import io.github.vincemann.demo.service.plugin.OwnerOfTheYearPlugin;
import io.github.vincemann.generic.crud.lib.service.CrudService;
import io.github.vincemann.generic.crud.lib.service.exception.BadEntityException;
import io.github.vincemann.generic.crud.lib.service.exception.EntityNotFoundException;
import io.github.vincemann.generic.crud.lib.service.exception.NoIdException;
import io.github.vincemann.generic.crud.lib.test.exception.InvalidConfigurationModificationException;
import io.github.vincemann.generic.crud.lib.test.service.eagerFetch.ForceEagerFetchCrudServiceIntegrationTest;
import io.github.vincemann.generic.crud.lib.test.service.callback.PostUpdateServiceTestCallback;
import io.github.vincemann.generic.crud.lib.test.service.crudTests.config.update.SuccessfulUpdateServiceTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static io.github.vincemann.generic.crud.lib.test.service.crudTests.config.ServiceTestConfigurations.partialUpdate;
import static io.github.vincemann.generic.crud.lib.test.service.crudTests.config.ServiceTestConfigurations.postUpdateCallback;

//@DataJpaTest cant be used because i need autowired components from generic-crud-lib
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test", "springdatajpa"})
class OwnerServiceIT
        extends ForceEagerFetchCrudServiceIntegrationTest<Owner, Long> {


    private Owner ownerWithoutPets;
    private Owner ownerWithOnePet;
    private Pet testPet;
    private PetType savedDogPetType;

    @SpyBean
    private OwnerOfTheYearPlugin ownerOfTheYearPlugin;

    @Autowired
    private CrudService<Pet, Long, PetRepository> petService;
    @Autowired
    private PetTypeService petTypeService;

    @BeforeEach
    public void setUp() throws Exception {
        //proxyfy service
        this.petService = getEagerFetchProxyFactory().create(petService);
        savedDogPetType = petTypeService.save(new PetType("Dog"));

        testPet = Pet.builder()
                .petType(savedDogPetType)
                .name("Bello")
                .birthDate(LocalDate.now())
                .build();

        ownerWithoutPets = Owner.builder()
                .firstName("owner without pets")
                .lastName("owner without pets lastName")
                .address("asljnflksamfslkmf")
                .city("n1 city")
                .telephone("12843723847324")
                .build();

        ownerWithOnePet = Owner.builder()
                .firstName("owner with one pet")
                .lastName("owner with one pet lastName")
                .address("asljnflksamfslkmf")
                .city("n1 city")
                .telephone("12843723847324")
                .pets(new HashSet<>(Arrays.asList(testPet)))
                .build();
    }

    @Test
    public void saveOwnerWithoutPets_ShouldSucceed() throws BadEntityException {
        getSaveServiceTest().saveEntity_ShouldSucceed(ownerWithoutPets);
    }

    @Test
    public void saveOwnerWithPet_ShouldSucceed() throws BadEntityException {
        getSaveServiceTest().saveEntity_ShouldSucceed(ownerWithOnePet);
    }

    @Test
    public void saveOwnerWithPersistedPet_ShouldSucceed() throws BadEntityException {
        Pet savedPet = petService.save(testPet);


        Owner owner = Owner.builder()
                .firstName("owner with one already persisted pet")
                .lastName("owner with one already persisted pet lastName")
                .address("asljnflksamfslkmf")
                .city("n1 city")
                .telephone("12843723847324")
                .pets(new HashSet<>(Arrays.asList(savedPet)))
                .build();
        getSaveServiceTest().saveEntity_ShouldSucceed(owner);
    }


    @Test
    public void updateOwner_ChangeTelephoneNumber_ShouldSucceed() throws BadEntityException, EntityNotFoundException, NoIdException, InvalidConfigurationModificationException {
        Owner diffTelephoneNumberUpdate = Owner.builder()
                .telephone(ownerWithoutPets.getTelephone() + "123")
                .build();
        getUpdateServiceTest().updateEntity_ShouldSucceed(ownerWithoutPets, diffTelephoneNumberUpdate,
                partialUpdate(),
                postUpdateCallback((request, afterUpdate) -> Assertions.assertEquals(request.getTelephone(), afterUpdate.getTelephone()))
        );
    }

    @Test
    public void updateOwner_addAnotherPet_shouldSucceed() throws BadEntityException, EntityNotFoundException, InvalidConfigurationModificationException, NoIdException {
        Pet savedPet = petService.save(testPet);
        String newPetName = "petToAdd";
        Pet newPet = Pet.builder()
                .name(newPetName)
                .petType(savedDogPetType)
                .birthDate(LocalDate.now())
                .build();
        Pet savedPetToAdd = petService.save(newPet);


        Owner owner = Owner.builder()
                .firstName("owner with one already persisted pet")
                .lastName("owner with one already persisted pet lastName")
                .address("asljnflksamfslkmf")
                .city("n1 city")
                .telephone("12843723847324")
                .pets(new HashSet<>(Arrays.asList(savedPet)))
                .build();

        Owner ownerUpdateRequest = new Owner();
        ownerUpdateRequest.getPets().addAll(owner.getPets());
        //here comes the new pet
        ownerUpdateRequest.getPets().add(savedPetToAdd);

        Owner updatedOwner = getUpdateServiceTest().updateEntity_ShouldSucceed(owner, ownerUpdateRequest,
                SuccessfulUpdateServiceTestConfiguration.<Owner, Long>builder()
                        .fullUpdate(false)
                        .postUpdateCallback((request, afterUpdate) -> {
                            Assertions.assertEquals(2, afterUpdate.getPets().size());
                            Assertions.assertEquals(1, afterUpdate.getPets().stream().filter(owner1 -> owner1.getName().equals(newPetName)).count());
                        })
                        .build());
    }

    @Test
    public void findOwnerOfTheYear_shouldSucceed_andTriggerPluginCallback() {
        //owner of the years name is 42
        ownerWithOnePet.setFirstName("42");
        Owner savedOwner = repoSave(ownerWithOnePet);
        OwnerService ownerService = getCastedCrudService();
        Optional<Owner> ownerOfTheYear = ownerService.findOwnerOfTheYear();
        Assertions.assertTrue(ownerOfTheYear.isPresent());
        Mockito.verify(ownerOfTheYearPlugin).onAfterFindOwnerOfTheYear(ownerOfTheYear);
    }

    @Test
    public void findByLastName_shouldSucceed() {
        Owner savedOwner = repoSave(ownerWithOnePet);
        OwnerService ownerService = getCastedCrudService();
        Optional<Owner> byLastName = ownerService.findByLastName(ownerWithOnePet.getLastName());
        Assertions.assertTrue(byLastName.isPresent());
        Assertions.assertTrue(getDefaultEqualChecker().isEqual(savedOwner, byLastName.get()));
    }


}