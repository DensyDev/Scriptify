package org.densy.scriptify.api.script.function;

import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.factory.ScriptFunctionDefinitionFactory;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

/**
 * Manages script functions, providing registration and retrieval capabilities.
 */
public interface ScriptFunctionManager {

    /**
     * Retrieves function definition factory.
     *
     * @return ScriptFunctionDefinitionFactory
     */
    ScriptFunctionDefinitionFactory getFunctionDefinitionFactory();

    /**
     * Set the function definition factory.
     *
     * @param factory ScriptFunctionDefinitionFactory
     */
    void setFunctionDefinitionFactory(ScriptFunctionDefinitionFactory factory);

    /**
     * Retrieves all registered functions.
     *
     * @return A map where keys are function names and values are ScriptFunction instances
     */
    @UnmodifiableView Map<String, ScriptFunctionDefinition> getFunctions();

    /**
     * Gets a specific function by its name.
     *
     * @param name The name of the function to retrieve
     * @return The ScriptFunction associated with the name, or null if not found
     */
    default @Nullable ScriptFunctionDefinition getFunction(String name) {
        return this.getFunctions().get(name);
    }

    /**
     * Registers a new function in the manager.
     *
     * @param function The function to be registered
     */
    void register(ScriptFunction function);

    /**
     * Removes an existing function in the manager.
     *
     * @param name The name of the function to remove
     */
    void remove(String name);
}
