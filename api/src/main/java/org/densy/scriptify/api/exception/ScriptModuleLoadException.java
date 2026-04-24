package org.densy.scriptify.api.exception;

/**
 * Custom exception for errors while script load process.
 */
public class ScriptModuleLoadException extends ScriptException {

    /**
     * Creates a new ScriptModuleLoadException with the specified message.
     *
     * @param message the detail message
     */
    public ScriptModuleLoadException(String message) {
        super(message);
    }

    /**
     * Creates a new ScriptModuleLoadException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ScriptModuleLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ScriptModuleLoadException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ScriptModuleLoadException(Throwable cause) {
        super(cause);
    }
}
