package org.densy.scriptify.api.exception;

/**
 * Custom exception for errors in script functions.
 */
public class ScriptException extends Exception {

    /**
     * Creates a new ScriptException with the specified message.
     *
     * @param message the detail message
     */
    public ScriptException(String message) {
        super(message);
    }

    /**
     * Creates a new ScriptException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ScriptException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ScriptException(Throwable cause) {
        super(cause);
    }
}
