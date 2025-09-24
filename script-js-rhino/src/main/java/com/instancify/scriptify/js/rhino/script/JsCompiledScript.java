package com.instancify.scriptify.js.rhino.script;

import com.instancify.scriptify.api.script.CompiledScript;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

public class JsCompiledScript implements CompiledScript<Object> {

    private final Context context;
    private final ScriptableObject scope;
    private final Script compiled;

    public JsCompiledScript(Context context, ScriptableObject scope, Script compiled) {
        this.context = context;
        this.scope = scope;
        this.compiled = compiled;
    }

    @Override
    public Object get() {
        return compiled.exec(context, scope);
    }

    @Override
    public Object eval(Object... args) {
        Object result = compiled.exec(context, scope);
        if (result instanceof Function function) {
            return function.call(context, scope, scope, args);
        }
        return result;
    }

    @Override
    public void close() {
        context.close();
    }
}
