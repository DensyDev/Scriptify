package org.densy.scriptify.core.script.function.definition;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionArgumentDefinition;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionExecutor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptFunctionDefinitionImpl implements ScriptFunctionDefinition {

    private final ScriptFunction function;
    private final List<ScriptFunctionExecutor> executors = new ArrayList<>();

    public ScriptFunctionDefinitionImpl(ScriptFunction function) {
        this.function = function;
        this.parseExecutors();
    }

    private void parseExecutors() {
        for (Method method : function.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(ExecuteAt.class)) continue;

            List<ScriptFunctionArgumentDefinition> arguments = new ArrayList<>();
            int executorIndex = -1;

            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                if (parameter.isAnnotationPresent(Executor.class)) {
                    if (executorIndex != -1) {
                        throw new IllegalStateException("Multiple @Executor parameters not allowed");
                    }
                    if (!Script.class.isAssignableFrom(parameter.getType())) {
                        throw new IllegalStateException("@Executor must be of type Script<?>");
                    }
                    executorIndex = i;
                    continue;
                }

                Argument argAnn = parameter.getAnnotation(Argument.class);
                if (argAnn != null) {
                    arguments.add(new ScriptFunctionArgumentDefinitionImpl(
                            argAnn.name(),
                            argAnn.required(),
                            parameter.getType()
                    ));
                } else {
                    throw new IllegalStateException("All parameters except @Executor must be annotated with @Argument");
                }
            }

            // Checking the order of arguments (required cannot come after optional)
            boolean foundOptional = false;
            for (ScriptFunctionArgumentDefinition def : arguments) {
                if (!def.isRequired()) {
                    foundOptional = true;
                } else if (foundOptional) {
                    throw new IllegalStateException("Required argument cannot follow an optional argument");
                }
            }

            method.setAccessible(true);
            executors.add(new ScriptFunctionExecutorImpl(function, method, executorIndex, arguments));
        }

        if (executors.isEmpty()) {
            throw new IllegalStateException("No executors defined");
        }
    }

    @Override
    public ScriptFunction getFunction() {
        return function;
    }

    @Override
    public List<ScriptFunctionExecutor> getExecutors() {
        return executors;
    }

    @Override
    public ScriptFunctionExecutor getExecutor(Class<?>... arguments) {
        for (ScriptFunctionExecutor executor : executors) {
            if (executor.matches(arguments)) {
                return executor;
            }
        }
        throw new IllegalArgumentException("No matching function for arguments: " +
                Arrays.stream(arguments).map(Class::getName).collect(Collectors.joining(", ")));
    }

}
