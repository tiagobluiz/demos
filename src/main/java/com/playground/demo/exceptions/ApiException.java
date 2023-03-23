package com.playground.demo.exceptions;

/**
 * Base class for all exceptions to be thrown by our API. This demands that all exceptions define a human-readable String
 * that enables any user to know what happened. This string will be exposed to API clients, so it should not contain any
 * sensitive information.
 */
public abstract class ApiException extends RuntimeException {

    public abstract String getReason();
}
