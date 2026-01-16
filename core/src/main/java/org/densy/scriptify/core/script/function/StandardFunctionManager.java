package org.densy.scriptify.core.script.function;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.ScriptFunctionManager;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.factory.ScriptFunctionDefinitionFactory;
import org.densy.scriptify.core.script.function.definition.factory.StandardFunctionDefinitionFactory;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StandardFunctionManager implements ScriptFunctionManager {

    private final Map<String, ScriptFunctionDefinition> functions = new HashMap<>();
    private ScriptFunctionDefinitionFactory functionDefinitionFactory;

    public StandardFunctionManager() {
        this(new StandardFunctionDefinitionFactory());
    }

    public StandardFunctionManager(ScriptFunctionDefinitionFactory functionDefinitionFactory) {
        this.setFunctionDefinitionFactory(functionDefinitionFactory);
    }

    @Override
    public ScriptFunctionDefinitionFactory getFunctionDefinitionFactory() {
        return functionDefinitionFactory;
    }

    @Override
    public void setFunctionDefinitionFactory(ScriptFunctionDefinitionFactory functionDefinitionFactory) {
        this.functionDefinitionFactory = Objects.requireNonNull(functionDefinitionFactory, "functionDefinitionFactory cannot be null");
    }

    @Override
    public @UnmodifiableView Map<String, ScriptFunctionDefinition> getFunctions() {
        return Collections.unmodifiableMap(functions);
    }

    @Override
    public void register(ScriptFunction function) {
        Objects.requireNonNull(function, "function cannot be null");
        if (!functions.containsKey(function.getName())) {
            ScriptFunctionDefinition definition = functionDefinitionFactory.create(function);
            if (definition == null) {
                throw new UnsupportedOperationException("Function definition factory has not created definition");
            }
            functions.put(function.getName(), definition);
        } else {
            throw new IllegalStateException("The function with name '" + function.getName() + "' already exists");
        }
    }

    @Override
    public void remove(String name) {
        if (functions.containsKey(name)) {
            functions.remove(name);
        } else {
            throw new IllegalArgumentException("The function with name '" + name + "' does not exist");
        }
    }
}