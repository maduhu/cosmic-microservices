package com.github.missioncriticalcloud.cosmic.metricscollector.exceptions;

public class UnableToCollectMetricsException extends RuntimeException {

    public UnableToCollectMetricsException() {
        super("Unable to collect metrics");
    }

    public UnableToCollectMetricsException(final String message) {
        super(message);
    }

    public UnableToCollectMetricsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnableToCollectMetricsException(final Throwable cause) {
        super(cause);
    }

}
