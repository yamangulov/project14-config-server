package org.satel.eip.project14.config.server.domain.config.internal.exception;

public class GetInternalConfigByClientException extends InternalConfigServiceGenericException {
    public GetInternalConfigByClientException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public GetInternalConfigByClientException(String errorMessage) {
        super(errorMessage);
    }

    public GetInternalConfigByClientException(Throwable err) {
        super(err);
    }

    public GetInternalConfigByClientException() {
        super();
    }
}
