package org.satel.eip.project14.config.server.metrics.accumulator.exception;


import org.satel.eip.project14.config.server.domain.generic.exception.GenericException;

public class AccumulatorRepositoryPostConstructException extends GenericException {
    public AccumulatorRepositoryPostConstructException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public AccumulatorRepositoryPostConstructException(String errorMessage) {
        super(errorMessage);
    }

    public AccumulatorRepositoryPostConstructException(Throwable err) {
        super(err);
    }

    public AccumulatorRepositoryPostConstructException() {
    }
}
