package org.densy.scriptify.api.exception;

/**
 * Exception thrown when a function is called with arguments
 * of incompatible types.
 */
public class ScriptFunctionArgumentTypeMismatchException extends ScriptFunctionException {

    /**
     * Creates a new exception for an argument type mismatch.
     *
     * @param functionName The name of the function
     * @param index The index of the argument that failed validation
     * @param expected The expected type of the argument
     * @param actual The actual type of the provided argument
     */
    public ScriptFunctionArgumentTypeMismatchException(
            String functionName,
            int index,
            Class<?> expected,
            Class<?> actual
    ) {
        super("Function '" + functionName + "' argument #" + index +
                " expected type " + expected.getName() +
                " but got " + (actual == null ? "null" : actual.getName()));
    }
}

