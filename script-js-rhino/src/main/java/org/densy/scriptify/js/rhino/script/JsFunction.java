package org.densy.scriptify.js.rhino.script;

import org.densy.scriptify.api.exception.ScriptFunctionException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionExecutor;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Array;
import java.util.Arrays;

public class JsFunction implements Function {

    private final Script<?> script;
    private final ScriptFunctionDefinition definition;

    public JsFunction(Script<?> script, ScriptFunctionDefinition definition) {
        this.script = script;
        this.definition = definition;
    }

    @Override
    public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] arguments) {
        Class<?>[] types = Arrays.stream(arguments)
                .map(arg -> arg == null ? Object.class : arg.getClass())
                .toArray(Class<?>[]::new);

        try {
            ScriptFunctionExecutor executor = findMatchingExecutor(arguments);
            return executor.execute(script, adaptArgsForExecutor(executor, arguments));
        } catch (ScriptFunctionException e) {
            throw Context.throwAsScriptRuntimeEx(e);
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

    @Override
    public Scriptable construct(Context context, Scriptable scriptable, Object[] objects) {
        return null;
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public Object get(String s, Scriptable scriptable) {
        return null;
    }

    @Override
    public Object get(int i, Scriptable scriptable) {
        return null;
    }

    @Override
    public boolean has(String s, Scriptable scriptable) {
        return false;
    }

    @Override
    public boolean has(int i, Scriptable scriptable) {
        return false;
    }

    @Override
    public void put(String s, Scriptable scriptable, Object o) {

    }

    @Override
    public void put(int i, Scriptable scriptable, Object o) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(int i) {

    }

    @Override
    public Scriptable getPrototype() {
        return null;
    }

    @Override
    public void setPrototype(Scriptable scriptable) {

    }

    @Override
    public Scriptable getParentScope() {
        return null;
    }

    @Override
    public void setParentScope(Scriptable scriptable) {

    }

    @Override
    public Object[] getIds() {
        return new Object[0];
    }

    @Override
    public Object getDefaultValue(Class<?> aClass) {
        return null;
    }

    @Override
    public boolean hasInstance(Scriptable scriptable) {
        return false;
    }
}
