package vincemann.github.generic.crud.lib.controller.errorHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vincemann.github.generic.crud.lib.controller.DTOCrudController;
import vincemann.github.generic.crud.lib.controller.exception.EntityMappingException;
import vincemann.github.generic.crud.lib.service.exception.BadEntityException;
import vincemann.github.generic.crud.lib.service.exception.EntityNotFoundException;
import vincemann.github.generic.crud.lib.service.exception.NoIdException;

//todo leider ist die annotation nicht inherited.. deswegen muss die immer an die impl ran
@RestControllerAdvice(assignableTypes = DTOCrudController.class)
@Slf4j
public class BasicDTOCrudControllerExceptionHandler extends ImprovedRestExceptionHandler implements DTOCrudControllerExceptionHandler<ApiError>{


    @Override
    public ResponseEntity<ApiError> handleEntityMappingException(EntityMappingException e) {
        logInternalServerError(e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "EntityDTO could not be mapped to Entity or vice versa",e));
    }

    @Override
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,
                e.getLocalizedMessage(), e));
    }

    @Override
    public ResponseEntity<ApiError> handleNoIdException(NoIdException e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
                "Id of Entity was not set, but was needed", e));
    }

    @Override
    public ResponseEntity<ApiError> handleUnknownException(Exception e) {
        logInternalServerError(e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Unknown ServerError occured in CrudController",e));
    }

    @Override
    public ResponseEntity<ApiError> handleBadEntityException(BadEntityException e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,
                "Entity sent by client was malformed",e));
    }

    private void logInternalServerError(Exception e){
        log.error("internal Server error occured: ",e);
    }

}