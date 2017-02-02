package com.github.missioncriticalcloud.cosmic.metricscollector.exceptions;

public class FailedToCollectMetricsException extends RuntimeException {

    public FailedToCollectMetricsException() {
        super("Failed to collect metrics.");
    }

    public FailedToCollectMetricsException(final String message) {
        super(message);
    }

    public FailedToCollectMetricsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FailedToCollectMetricsException(final Throwable cause) {
        super(cause);
    }

}
