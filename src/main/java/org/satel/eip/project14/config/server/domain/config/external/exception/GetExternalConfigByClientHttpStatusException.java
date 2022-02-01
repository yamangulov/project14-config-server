package org.satel.eip.project14.config.server.domain.config.external.exception;

public class GetExternalConfigByClientHttpStatusException extends ExternalConfigServiceGenericException {
    public GetExternalConfigByClientHttpStatusException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public GetExternalConfigByClientHttpStatusException(String errorMessage) {
        super(errorMessage);
    }

    public GetExternalConfigByClientHttpStatusException(Throwable err) {
        super(err);
    }

    public GetExternalConfigByClientHttpStatusException() {
        super();
    }
}
