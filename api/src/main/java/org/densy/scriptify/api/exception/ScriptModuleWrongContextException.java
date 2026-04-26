package org.densy.scriptify.api.exception;

/**
 * Custom exception for errors when script module context mismatch occurs.
 */
public class ScriptModuleWrongContextException extends ScriptException {

    /**
     * Creates a new ScriptModuleWrongContextException  with the specified message.
     *
     * @param expected the expected context
     * @param actual the given context
     */
    public ScriptModuleWrongContextException(Class<?> expected, Class<?> actual) {
        super("Expected context of type " + expected.getName() + " but got " + actual.getName());
    }
}
