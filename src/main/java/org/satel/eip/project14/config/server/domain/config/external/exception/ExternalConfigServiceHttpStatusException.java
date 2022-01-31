package org.satel.eip.project14.config.server.domain.config.external.exception;

public class ExternalConfigServiceHttpStatusException extends ExternalConfigServiceGenericException {
    public ExternalConfigServiceHttpStatusException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ExternalConfigServiceHttpStatusException(String errorMessage) {
        super(errorMessage);
    }

    public ExternalConfigServiceHttpStatusException(Throwable err) {
        super(err);
    }

    public ExternalConfigServiceHttpStatusException() {
        super();
    }
}
