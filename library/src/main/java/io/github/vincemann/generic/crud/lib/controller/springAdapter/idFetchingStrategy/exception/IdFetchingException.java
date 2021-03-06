package io.github.vincemann.generic.crud.lib.controller.springAdapter.idFetchingStrategy.exception;

import io.github.vincemann.generic.crud.lib.controller.springAdapter.idFetchingStrategy.IdFetchingStrategy;

/**
 * Indicates that the id could not be Fetched from the httprequest.
 * See : {@link IdFetchingStrategy}
 */
public class IdFetchingException extends Exception {
    public IdFetchingException() {
    }

    public IdFetchingException(String message) {
        super(message);
    }

    public IdFetchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdFetchingException(Throwable cause) {
        super(cause);
    }

    public IdFetchingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
