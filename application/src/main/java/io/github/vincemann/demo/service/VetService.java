package io.github.vincemann.demo.service;

import io.github.vincemann.demo.model.Vet;
import io.github.vincemann.demo.repositories.VetRepository;
import io.github.vincemann.generic.crud.lib.service.CrudService;

public interface VetService extends CrudService<Vet,Long, VetRepository> {
}
