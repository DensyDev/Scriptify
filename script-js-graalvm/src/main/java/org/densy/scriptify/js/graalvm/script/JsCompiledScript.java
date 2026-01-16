package org.densy.scriptify.js.graalvm.script;

import org.densy.scriptify.api.script.CompiledScript;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class JsCompiledScript implements CompiledScript<Value> {
    private final Context context;
    private final Value value;

    public JsCompiledScript(Context context, Value value) {
        this.context = context;
        this.value = value;
    }

    @Override
    public Value get() {
        return value;
    }

    @Override
    public Value eval(Object... args) {
        if (!value.canExecute()) {
            throw new IllegalStateException("Script is not executable");
        }
        return value.execute(args);
    }

    @Override
    public void close() {
        context.close();
    }
}

