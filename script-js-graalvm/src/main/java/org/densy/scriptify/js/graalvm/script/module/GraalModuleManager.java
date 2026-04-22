package org.densy.scriptify.js.graalvm.script.module;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.ScriptModuleManager;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.densy.scriptify.core.script.module.ScriptGlobalModule;
import org.densy.scriptify.core.script.module.export.ScriptConstantExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.densy.scriptify.js.graalvm.script.JsFunction;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GraalModuleManager implements ScriptModuleManager {

    private final Script<?> script;
    private final ScriptGlobalModule globalModule = new ScriptGlobalModule();
    private final Map<String, ScriptModule> modules = new LinkedHashMap<>();

    public GraalModuleManager(Script<?> script) {
        this.script = script;
    }

    @Override
    public ScriptGlobalModule getGlobalModule() {
        return globalModule;
    }

    @Override
    public ScriptModule getModule(String name) {
        return modules.get(name);
    }

    @Override
    public void addModule(ScriptModule module) {
        Objects.requireNonNull(module, "module cannot be null");
        Objects.requireNonNull(module.getName(), "module name cannot be null");
        modules.put(module.getName(), module);
    }

    @Override
    public void removeModule(String name) {
        modules.remove(name);
    }

    public void applyTo(Context context) {
        Value bindings = context.getBindings("js");

        for (ScriptExport export : globalModule.getExports()) {
            bindings.putMember(export.getName(), resolveValue(context, export));
        }
    }

    private Object resolveValue(Context context, ScriptExport export) {
        if (export instanceof ScriptValueExport valueExport) {
            return context.asValue(valueExport.getValue());
        }
        if (export instanceof ScriptFunctionExport functionExport) {
            return new JsFunction(script, functionExport.getDefinition());
        }
        if (export instanceof ScriptConstantExport constantExport) {
            return constantExport.getConstant().getValue();
        }
        throw new UnsupportedOperationException("Unsupported export type: " + export.getClass().getName()   );
    }
}
