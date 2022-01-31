package org.satel.eip.project14.config.server.domain.config.internal.exception;

public class InternalConfigServiceGetInternalConfigByClientException extends InternalConfigServiceGenericException {
    public InternalConfigServiceGetInternalConfigByClientException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InternalConfigServiceGetInternalConfigByClientException(String errorMessage) {
        super(errorMessage);
    }

    public InternalConfigServiceGetInternalConfigByClientException(Throwable err) {
        super(err);
    }

    public InternalConfigServiceGetInternalConfigByClientException() {
        super();
    }
}
