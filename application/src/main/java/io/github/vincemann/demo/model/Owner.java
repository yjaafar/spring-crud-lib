package io.github.vincemann.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.vincemann.demo.model.abs.Person;
import io.github.vincemann.generic.crud.lib.jpaAuditing.entityListener.BiDirParentEntityListener;
import io.github.vincemann.generic.crud.lib.model.IdentifiableEntityImpl;
import io.github.vincemann.generic.crud.lib.model.biDir.child.BiDirChildCollection;
import io.github.vincemann.generic.crud.lib.model.biDir.parent.BiDirParent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "owners")
@EntityListeners(BiDirParentEntityListener.class)
public class Owner extends Person implements BiDirParent {


    @Builder
    public Owner(String firstName, String lastName, Set<Pet> pets, String address, String city, String telephone) {
        super(firstName, lastName);
        if(pets!=null) {
            this.pets = pets;
        }
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner",fetch = FetchType.EAGER)
    @JsonManagedReference
    @BiDirChildCollection(Pet.class)
    private Set<Pet> pets = new HashSet<>();


    @Column(name = "adress")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @Override
    public String toString() {
        return "Owner{" +
                "petIds=" + Arrays.toString(pets.stream().map(IdentifiableEntityImpl::getId).toArray()) +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
