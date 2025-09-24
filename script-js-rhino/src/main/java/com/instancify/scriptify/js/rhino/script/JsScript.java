package com.instancify.scriptify.js.rhino.script;

import com.instancify.scriptify.api.exception.ScriptException;
import com.instancify.scriptify.api.script.CompiledScript;
import com.instancify.scriptify.api.script.Script;
import com.instancify.scriptify.api.script.constant.ScriptConstant;
import com.instancify.scriptify.api.script.constant.ScriptConstantManager;
import com.instancify.scriptify.api.script.function.ScriptFunctionManager;
import com.instancify.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import com.instancify.scriptify.api.script.security.ScriptSecurityManager;
import com.instancify.scriptify.core.script.constant.StandardConstantManager;
import com.instancify.scriptify.core.script.function.StandardFunctionManager;
import com.instancify.scriptify.core.script.security.StandardSecurityManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsScript implements Script<Object> {

    private final ScriptSecurityManager securityManager = new StandardSecurityManager();
    private ScriptFunctionManager functionManager = new StandardFunctionManager();
    private ScriptConstantManager constantManager = new StandardConstantManager();
    private final List<String> extraScript = new ArrayList<>();

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
    public ScriptConstantManager getConstantManager() {
        return constantManager;
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
    public CompiledScript<Object> compile(String script) throws ScriptException {
        try {
            Context context = Context.enter();
            context.setWrapFactory(new JsWrapFactory());

            ScriptableObject scope = context.initStandardObjects();

            // If security mode is enabled, search all exclusions
            // and add the classes that were excluded to JsSecurityClassAccessor
            if (securityManager.getSecurityMode()) {
                context.setClassShutter(new JsSecurityClassAccessor(securityManager.getExcludes()));
            }

            for (ScriptFunctionDefinition definition : functionManager.getFunctions().values()) {
                scope.put(definition.getFunction().getName(), scope, new JsFunction(this, definition));
            }

            for (ScriptConstant constant : constantManager.getConstants().values()) {
                ScriptableObject.putConstProperty(scope, constant.getName(), constant.getValue());
            }

            // Building full script including extra script code
            StringBuilder fullScript = new StringBuilder();
            for (String extra : extraScript) {
                fullScript.append(extra).append("\n");
            }
            fullScript.append(script);

            var compiled = context.compileString(fullScript.toString(), "script", 1, null);
            return new JsCompiledScript(context, scope, compiled);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Object evalOneShot(String script) throws ScriptException {
        try (CompiledScript<Object> compiled = compile(script)) {
            return compiled.get();
        }
    }
}
