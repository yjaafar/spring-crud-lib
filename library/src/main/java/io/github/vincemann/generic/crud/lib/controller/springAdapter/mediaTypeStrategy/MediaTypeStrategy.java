package io.github.vincemann.generic.crud.lib.controller.springAdapter.mediaTypeStrategy;

import io.github.vincemann.generic.crud.lib.model.IdentifiableEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Interface for 'how to convert the stringBody of an HttpRequest to a Dto Entity
 */
public interface MediaTypeStrategy<Id extends Serializable> {
    /**
     *
     * @param body      body of HttpRequest
     * @param dtoClass  class of the Dto Entity
     * @param <Dto>     Type of Dto Entity
     * @return          Dto entity
     * @throws DtoReadingException      occurs, when Dto could not be fetched from StringBody
     */
    <Dto extends IdentifiableEntity<Id>> Dto readDtoFromBody(String body, Class<Dto> dtoClass) throws DtoReadingException;

    <Dto extends IdentifiableEntity<Id>, C extends Collection<Dto>> C readDtosFromBody(String body, Class<Dto> dtoClass, Class<C> collectionType) throws DtoReadingException;

    /**
     * Use {@link org.springframework.http.MediaType}
     * @return     MediaType used to represent Dto in the body (mostly JSON, or XML)
     */
    String getMediaType();

    /**
     *
     * @param body      body of HttpRequest
     * @param type      type to be checked
     * @return
     */
    boolean isBodyOfGivenType(String body, Class type);
}
