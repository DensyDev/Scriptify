package org.densy.scriptify.core.script.function.definition;

import org.densy.scriptify.api.exception.ScriptFunctionArgumentCountMismatchException;
import org.densy.scriptify.api.exception.ScriptFunctionArgumentTypeMismatchException;
import org.densy.scriptify.api.exception.ScriptFunctionException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionArgumentDefinition;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionExecutor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.List;

@Getter
public class ScriptFunctionExecutorImpl implements ScriptFunctionExecutor {

    private final ScriptFunction owner;
    private final Method method;
    private final int executorIndex;
    private final List<ScriptFunctionArgumentDefinition> arguments;

    public ScriptFunctionExecutorImpl(ScriptFunction owner, Method method, int executorIndex, List<ScriptFunctionArgumentDefinition> arguments) {
        this.owner = owner;
        this.method = method;
        this.executorIndex = executorIndex;
        this.arguments = arguments;
    }

    @Override
    public boolean matches(Class<?>... types) {
        if (types.length > arguments.size()) return false;

        for (int i = 0; i < types.length; i++) {
            if (!arguments.get(i).getType().isAssignableFrom(types[i])) {
                return false;
            }
        }

        for (int i = types.length; i < arguments.size(); i++) {
            if (arguments.get(i).isRequired()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object execute(Script<?> script, Object... args) throws ScriptFunctionException {
        // Checking the number of arguments
        int minArgs = (int) arguments.stream().filter(ScriptFunctionArgumentDefinition::isRequired).count();
        int maxArgs = arguments.size();
        if (args.length < minArgs || args.length > maxArgs) {
            throw new ScriptFunctionArgumentCountMismatchException(
                    owner.getName(), minArgs, maxArgs, args.length
            );
        }

        // Checking argument types
        for (int i = 0; i < args.length; i++) {
            Class<?> expected = arguments.get(i).getType();
            Object actual = args[i];
            if (actual != null && !expected.isAssignableFrom(actual.getClass())) {
                throw new ScriptFunctionArgumentTypeMismatchException(
                        owner.getName(), i, expected, actual.getClass()
                );
            }
        }

        try {
            Object[] finalArgs;

            if (executorIndex >= 0) {
                finalArgs = new Object[arguments.size() + 1];
                for (int i = 0, j = 0; i < finalArgs.length; i++) {
                    if (i == executorIndex) {
                        finalArgs[i] = script;
                    } else {
                        finalArgs[i] = (j < args.length) ? args[j] : null;
                        j++;
                    }
                }
            } else {
                finalArgs = args;
            }

            return method.invoke(owner, finalArgs);
        } catch (Exception e) {
            throw new ScriptFunctionException("Failed to execute script function '" + owner.getName() + "'", e);
        }
    }
}
