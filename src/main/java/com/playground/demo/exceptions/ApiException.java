package com.playground.demo.exceptions;

import java.util.Map;

/**
 * Base class for all exceptions to be thrown by our API. This includes the status code to be thrown, the reason,
 * a human-readable string explaining the error, and a list of values that led to this exception.
 */
public abstract class ApiException extends RuntimeException {

    public abstract int getStatus();

    public abstract String getReason();

    public Map<String, Object> getFaultyValues() {
        return Map.of();
    }
}
