package io.github.vincemann.demo.service.springDataJPA;

import io.github.vincemann.demo.jpaRepositories.VisitRepository;
import io.github.vincemann.demo.model.Visit;
import io.github.vincemann.demo.service.VisitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import io.github.vincemann.generic.crud.lib.service.springDataJpa.JPACrudService;

@Service
@Profile("springdatajpa")
public class VisitJPAService extends JPACrudService<Visit,Long, VisitRepository> implements VisitService {

    public VisitJPAService(VisitRepository jpaRepository) {
        super(jpaRepository, Visit.class);
    }
}