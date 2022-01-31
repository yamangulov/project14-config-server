package org.satel.eip.project14.config.server.domain.config.external.exception;

import org.satel.eip.project14.config.server.domain.generic.exception.GenericException;

public class ExternalConfigServiceGenericException extends GenericException {
    public ExternalConfigServiceGenericException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ExternalConfigServiceGenericException(String errorMessage) {
        super(errorMessage);
    }

    public ExternalConfigServiceGenericException(Throwable err) {
        super(err);
    }

    public ExternalConfigServiceGenericException() {
        super();
    }
}
