package com.lakienkoigor.user.archive.exception;

/**
 * Thrown when object isn't found.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * @param message description of exception.
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * @param message description of exception.
     * @param cause   throwable object.
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
