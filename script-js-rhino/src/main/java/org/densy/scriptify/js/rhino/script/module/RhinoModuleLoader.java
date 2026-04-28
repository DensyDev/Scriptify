package org.densy.scriptify.js.rhino.script.module;

import org.densy.scriptify.api.exception.ScriptModuleLoadException;
import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;
import org.densy.scriptify.api.script.module.ScriptExternalModule;
import org.densy.scriptify.api.script.module.ScriptInternalModule;
import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class RhinoModuleLoader extends BaseFunction {

    private final RhinoModuleManager moduleManager;
    private final Context context;
    private final ScriptableObject scope;
    private final ScriptModuleExportResolver resolver;
    private final Map<String, ScriptableObject> moduleCache = new HashMap<>();

    public RhinoModuleLoader(RhinoModuleManager moduleManager, Context context, ScriptableObject scope) {
        this.moduleManager = moduleManager;
        this.context = context;
        this.scope = scope;
        try {
            this.resolver = moduleManager.getModuleExportResolver().create(new RhinoModuleContext(context, scope));
        } catch (ScriptModuleWrongContextException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Module name is required");
        }
        return load(Context.toString(args[0]));
    }

    public ScriptableObject load(String moduleName) {
        ScriptableObject cached = moduleCache.get(moduleName);
        if (cached != null) {
            return cached;
        }

        ScriptModule module = moduleManager.getModule(moduleName);
        if (module instanceof ScriptInternalModule internalModule) {
            return loadInternal(moduleName, internalModule);
        }
        if (module instanceof ScriptExternalModule externalModule) {
            return loadExternal(moduleName, externalModule);
        }
        throw Context.reportRuntimeError("Script module not found: " + moduleName);
    }

    private ScriptableObject loadInternal(String moduleName, ScriptInternalModule module) {
        ScriptableObject moduleObject = createModuleObject();
        moduleCache.put(moduleName, moduleObject);

        for (ScriptExport export : module.getExports()) {
            ScriptableObject.putProperty(moduleObject, export.getName(), resolver.resolve(export));
        }

        return moduleObject;
    }

    private ScriptableObject loadExternal(String moduleName, ScriptExternalModule module) {
        ScriptableObject exports = createModuleObject();
        moduleCache.put(moduleName, exports);

        try {
            String source = new String(module.load(), StandardCharsets.UTF_8);
            String transformed = RhinoModuleSourceTransformer.transformModule(source);
            Object wrapper = context.evaluateString(
                    scope,
                    "(function(exports, __scriptify_require) {\n" + transformed + "\n})",
                    module.getSourceName(),
                    1,
                    null
            );
            if (!(wrapper instanceof Function function)) {
                throw Context.reportRuntimeError("External module did not compile to a function: " + moduleName);
            }
            function.call(context, scope, scope, new Object[]{exports, this});
            return exports;
        } catch (ScriptModuleLoadException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    private ScriptableObject createModuleObject() {
        ScriptableObject object = (ScriptableObject) context.newObject(scope);
        object.setPrototype(ScriptableObject.getObjectPrototype(scope));
        object.setParentScope(scope);
        return object;
    }
}
