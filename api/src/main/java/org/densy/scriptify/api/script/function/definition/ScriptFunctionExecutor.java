package org.densy.scriptify.api.script.function.definition;

import org.densy.scriptify.api.exception.ScriptFunctionException;
import org.densy.scriptify.api.script.Script;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents an executable method of a script function,
 * responsible for invoking the annotated logic.
 */
public interface ScriptFunctionExecutor {

    /**
     * Retrieves the executor method.
     *
     * @return A {@link Method}
     */
    Method getMethod();

    /**
     * Retrieves the argument definitions for this executor.
     *
     * @return A list of {@link ScriptFunctionArgumentDefinition}
     */
    List<ScriptFunctionArgumentDefinition> getArguments();

    /**
     * Checks whether this executor matches the given argument types.
     *
     * @param types the argument types to match
     * @return true if the executor is compatible, false otherwise
     */
    boolean matches(Class<?>... types);

    /**
     * Executes this function with the given script context and arguments.
     *
     * @param script The script instance to pass to the executor
     * @param args The function arguments
     * @return The result of execution
     * @throws ScriptFunctionException if execution fails or arguments are invalid
     */
    Object execute(Script<?> script, Object... args) throws ScriptFunctionException;
}
