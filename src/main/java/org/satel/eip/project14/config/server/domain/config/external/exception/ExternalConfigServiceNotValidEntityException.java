package org.satel.eip.project14.config.server.domain.config.external.exception;

public class ExternalConfigServiceNotValidEntityException extends ExternalConfigServiceGenericException {
    public ExternalConfigServiceNotValidEntityException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ExternalConfigServiceNotValidEntityException(String errorMessage) {
        super(errorMessage);
    }

    public ExternalConfigServiceNotValidEntityException(Throwable err) {
        super(err);
    }

    public ExternalConfigServiceNotValidEntityException() {
        super();
    }
}
