package org.satel.eip.project14.config.server.domain.config.external.exception;

public class GetExternalConfigByClientNotValidEntityException extends ExternalConfigServiceGenericException {
    public GetExternalConfigByClientNotValidEntityException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public GetExternalConfigByClientNotValidEntityException(String errorMessage) {
        super(errorMessage);
    }

    public GetExternalConfigByClientNotValidEntityException(Throwable err) {
        super(err);
    }

    public GetExternalConfigByClientNotValidEntityException() {
        super();
    }
}
