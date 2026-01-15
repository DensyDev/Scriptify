package org.densy.scriptify.api.exception;

/**
 * Custom exception for errors in script functions.
 */
public class ScriptFunctionException extends ScriptException {

    /**
     * Creates a new ScriptFunctionException with the specified message.
     *
     * @param message the detail message
     */
    public ScriptFunctionException(String message) {
        super(message);
    }

    /**
     * Creates a new ScriptFunctionException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ScriptFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ScriptFunctionException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ScriptFunctionException(Throwable cause) {
        super(cause);
    }
}
