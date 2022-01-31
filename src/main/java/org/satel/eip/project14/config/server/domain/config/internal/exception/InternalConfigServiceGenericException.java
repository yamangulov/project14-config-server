package org.satel.eip.project14.config.server.domain.config.internal.exception;

import org.satel.eip.project14.config.server.domain.generic.exception.GenericException;

public class InternalConfigServiceGenericException extends GenericException {
    public InternalConfigServiceGenericException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InternalConfigServiceGenericException(String errorMessage) {
        super(errorMessage);
    }

    public InternalConfigServiceGenericException(Throwable err) {
        super(err);
    }

    public InternalConfigServiceGenericException() {
        super();
    }
}
