package org.densy.scriptify.js.graalvm.script;

import org.densy.scriptify.api.exception.ScriptException;
import org.densy.scriptify.api.script.CompiledScript;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.ScriptObject;
import org.densy.scriptify.api.script.constant.ScriptConstant;
import org.densy.scriptify.api.script.constant.ScriptConstantManager;
import org.densy.scriptify.api.script.function.ScriptFunctionManager;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.security.ScriptSecurityManager;
import org.densy.scriptify.core.script.constant.StandardConstantManager;
import org.densy.scriptify.core.script.function.StandardFunctionManager;
import org.densy.scriptify.core.script.security.StandardSecurityManager;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsScript implements Script<Value> {

    private final ScriptSecurityManager securityManager = new StandardSecurityManager();
    private ScriptFunctionManager functionManager = new StandardFunctionManager();
    private ScriptConstantManager constantManager = new StandardConstantManager();
    private final List<String> extraScript = new ArrayList<>();

    @Override
    public ScriptConstantManager getConstantManager() {
        return constantManager;
    }

    @Override
    public ScriptSecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public ScriptFunctionManager getFunctionManager() {
        return functionManager;
    }

    @Override
    public void setFunctionManager(ScriptFunctionManager functionManager) {
        this.functionManager = Objects.requireNonNull(functionManager, "functionManager cannot be null");
    }

    @Override
    public void setConstantManager(ScriptConstantManager constantManager) {
        this.constantManager = Objects.requireNonNull(constantManager, "constantManager cannot be null");
    }

    @Override
    public void addExtraScript(String script) {
        this.extraScript.add(script);
    }

    @Override
    public CompiledScript<Value> compile(String script) throws ScriptException {
        Context.Builder builder = Context.newBuilder("js")
                .allowHostAccess(HostAccess.newBuilder(HostAccess.ALL)
                        // Mapping for the ScriptObject class required
                        // to convert a ScriptObject to the value it contains
                        .targetTypeMapping(
                                ScriptObject.class,
                                Object.class,
                                object -> true,
                                ScriptObject::getValue
                        )
                        .build());

        // If security mode is enabled, search all exclusions
        // and add the classes that were excluded to JsSecurityClassAccessor
        if (securityManager.getSecurityMode()) {
            builder.allowHostClassLookup(new JsSecurityClassAccessor(securityManager.getExcludes()));
        } else {
            builder.allowHostClassLookup(className -> true);
        }

        Context context = builder.build();

        Value bindings = context.getBindings("js");

        for (ScriptFunctionDefinition definition : functionManager.getFunctions().values()) {
            bindings.putMember(definition.getFunction().getName(), new JsFunction(this, definition));
        }

        for (ScriptConstant constant : constantManager.getConstants().values()) {
            bindings.putMember(constant.getName(), constant.getValue());
        }

        // Building full script including extra script code
        StringBuilder fullScript = new StringBuilder();
        for (String extra : extraScript) {
            fullScript.append(extra).append("\n");
        }
        fullScript.append(script);

        try {
            return new JsCompiledScript(context, context.eval("js", fullScript.toString()));
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Value evalOneShot(String script) throws ScriptException {
        try (CompiledScript<Value> compiled = compile(script)) {
            return compiled.get();
        }
    }
}
