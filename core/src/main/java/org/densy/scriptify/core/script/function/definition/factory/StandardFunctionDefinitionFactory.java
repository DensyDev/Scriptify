package org.densy.scriptify.core.script.function.definition.factory;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.factory.ScriptFunctionDefinitionFactory;
import org.densy.scriptify.core.script.function.definition.ScriptFunctionDefinitionImpl;

public class StandardFunctionDefinitionFactory implements ScriptFunctionDefinitionFactory {

    @Override
    public ScriptFunctionDefinition create(ScriptFunction function) {
        return new ScriptFunctionDefinitionImpl(function);
    }
}
