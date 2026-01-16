package org.densy.scriptify.api.script.function.definition.factory;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;

/**
 * Script function definition factory.
 * Allows to create custom factory for creating specific definitions.
 */
public interface ScriptFunctionDefinitionFactory {

    /**
     * Creates a function definition.
     *
     * @param function ScriptFunction
     * @return ScriptFunctionDefinition
     */
    ScriptFunctionDefinition create(ScriptFunction function);
}
