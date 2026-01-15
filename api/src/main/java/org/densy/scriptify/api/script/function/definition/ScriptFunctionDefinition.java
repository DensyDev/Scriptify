package org.densy.scriptify.api.script.function.definition;

import org.densy.scriptify.api.script.function.ScriptFunction;

import java.util.List;

/**
 * Represents the definition of a {@link ScriptFunction}, including
 * its available executors parsed from annotated methods.
 */
public interface ScriptFunctionDefinition {

    /**
     * Retrieves the underlying script function.
     *
     * @return The associated {@link ScriptFunction}
     */
    ScriptFunction getFunction();

    /**
     * Retrieves all executors available for this function.
     *
     * @return A list of {@link ScriptFunctionExecutor} instances
     */
    List<ScriptFunctionExecutor> getExecutors();

    /**
     * Finds an executor matching the given argument types.
     *
     * @param arguments the argument types to match
     * @return the matching {@link ScriptFunctionExecutor}
     * @throws IllegalArgumentException if you cannot find a matching executor
     */
    ScriptFunctionExecutor getExecutor(Class<?>... arguments);
}
