package org.densy.scriptify.api.exception;

/**
 * Exception thrown when a function is called with
 * an incorrect number of arguments.
 */
public class ScriptFunctionArgumentCountMismatchException extends ScriptFunctionException {

    /**
     * Creates a new exception for a mismatch in argument count.
     *
     * @param functionName The name of the function
     * @param expectedMin The minimum number of expected arguments
     * @param expectedMax The maximum number of expected arguments
     * @param provided The number of arguments actually provided
     */
    public ScriptFunctionArgumentCountMismatchException(
            String functionName,
            int expectedMin,
            int expectedMax,
            int provided
    ) {
        super("Function '" + functionName + "' expects between " +
                expectedMin + " and " + expectedMax + " arguments, but got " + provided);
    }
}
