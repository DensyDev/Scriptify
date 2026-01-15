package org.densy.scriptify.api.script.function;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a function that can be used within scripts.
 */
public interface ScriptFunction {

    /**
     * Gets the name of the function.
     *
     * @return The function's name
     */
    @NotNull String getName();
}
