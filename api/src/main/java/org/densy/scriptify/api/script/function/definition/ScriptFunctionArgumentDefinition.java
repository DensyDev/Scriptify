package org.densy.scriptify.api.script.function.definition;

/**
 * Represents a single argument in a script function definition.
 */
public interface ScriptFunctionArgumentDefinition {

    /**
     * Retrieves the name of this argument.
     *
     * @return The argument name
     */
    String getName();

    /**
     * Indicates whether this argument is required.
     *
     * @return true if the argument must be provided, false if it is optional
     */
    boolean isRequired();

    /**
     * Retrieves the expected Java type of this argument.
     *
     * @return The argument type
     */
    Class<?> getType();
}
