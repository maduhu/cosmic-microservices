package com.github.missioncriticalcloud.cosmic.api.usage.exceptions;

public class UnableToSearchIndexException extends RuntimeException {

    public UnableToSearchIndexException() {
        super("Unable to search on the index");
    }

    public UnableToSearchIndexException(final String message) {
        super(message);
    }

    public UnableToSearchIndexException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnableToSearchIndexException(final Throwable cause) {
        super(cause);
    }

}
