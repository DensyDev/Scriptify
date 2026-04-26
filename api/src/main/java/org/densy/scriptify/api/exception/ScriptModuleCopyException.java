package org.densy.scriptify.api.exception;

/**
 * Custom exception for errors while script copy process.
 */
public class ScriptModuleCopyException extends RuntimeException {

    /**
     * Creates a new ScriptModuleCopyException with the specified message.
     *
     * @param message the detail message
     */
    public ScriptModuleCopyException(String message) {
        super(message);
    }

    /**
     * Creates a new ScriptModuleCopyException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ScriptModuleCopyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ScriptModuleCopyException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ScriptModuleCopyException(Throwable cause) {
        super(cause);
    }
}
