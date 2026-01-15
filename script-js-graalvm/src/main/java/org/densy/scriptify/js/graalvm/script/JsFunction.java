package org.densy.scriptify.js.graalvm.script;

import org.densy.scriptify.api.exception.ScriptFunctionException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionExecutor;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsFunction implements ProxyExecutable {

    private final Script<?> script;
    private final ScriptFunctionDefinition definition;

    public JsFunction(Script<?> script, ScriptFunctionDefinition definition) {
        this.script = script;
        this.definition = definition;
    }

    @Override
    public Object execute(Value... arguments) {
        Object[] args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = convertValue(arguments[i]);
        }

        try {
            ScriptFunctionExecutor executor = findMatchingExecutor(args);
            return executor.execute(script, adaptArgsForExecutor(executor, args));
        } catch (ScriptFunctionException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptFunctionExecutor findMatchingExecutor(Object[] args) {
        for (ScriptFunctionExecutor executor : definition.getExecutors()) {
            int paramCount = executor.getArguments().size();
            if (executor.getMethod().isVarArgs()) {
                if (args.length >= paramCount - 1) {
                    return executor;
                }
            } else {
                if (args.length == paramCount) {
                    return executor;
                }
            }
        }
        throw new IllegalArgumentException("No matching executor found for arguments");
    }

    private Object[] adaptArgsForExecutor(ScriptFunctionExecutor executor, Object[] args) {
        var method = executor.getMethod();
        var paramTypes = method.getParameterTypes();

        if (paramTypes.length == 0) {
            return new Object[0];
        }

        if (!method.isVarArgs()) {
            return args;
        }

        var fixedCount = paramTypes.length - 1;
        var finalArgs = new Object[paramTypes.length];

        for (int i = 0; i < fixedCount; i++) {
            finalArgs[i] = i < args.length ? args[i] : null;
        }

        var varargType = paramTypes[fixedCount].getComponentType();
        var varargCount = Math.max(0, args.length - fixedCount);
        var varargArray = Array.newInstance(varargType, varargCount);

        for (int i = 0; i < varargCount; i++) {
            Array.set(varargArray, i, args[fixedCount + i]);
        }

        finalArgs[fixedCount] = varargArray;
        return finalArgs;
    }

    /**
     * Converts the Value object to the appropriate Java type.
     */
    private Object convertValue(Value value) {
        if (value.isNull()) {
            return null;
        } else if (value.hasArrayElements()) {
            List<Object> list = new ArrayList<>();
            for (long j = 0; j < value.getArraySize(); j++) {
                list.add(convertValue(value.getArrayElement(j)));
            }
            return list;
        } else if (value.isHostObject()) {
            return value.asHostObject();
        } else if (value.isString()) {
            return value.asString();
        } else if (value.isBoolean()) {
            return value.asBoolean();
        } else if (value.isNumber()) {
            return value.as(Number.class);
        } else {
            return value.as(Object.class);
        }
    }
}
