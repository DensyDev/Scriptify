package org.densy.scriptify.js.rhino.script.module;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.ScriptModuleManager;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.access.ScriptAccess;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolverFactory;
import org.densy.scriptify.core.script.module.ScriptInternalGlobalModule;
import org.densy.scriptify.js.rhino.script.module.export.resolver.RhinoModuleExportResolverFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RhinoModuleManager implements ScriptModuleManager {

    private static final String REQUIRE_NAME = "__scriptify_require";

    private final ScriptInternalGlobalModule globalModule = new ScriptInternalGlobalModule();
    private final Map<String, ScriptModule> modules = new LinkedHashMap<>();
    private ScriptModuleExportResolverFactory moduleExportResolverFactory;
    private ScriptAccess scriptAccess = ScriptAccess.ALL;

    public RhinoModuleManager(Script<?> script) {
        this.setModuleExportResolver(new RhinoModuleExportResolverFactory(script));
    }

    @Override
    public ScriptModuleExportResolverFactory getModuleExportResolver() {
        return moduleExportResolverFactory;
    }

    @Override
    public void setModuleExportResolver(ScriptModuleExportResolverFactory moduleExportResolverFactory) {
        this.moduleExportResolverFactory = Objects.requireNonNull(moduleExportResolverFactory, "moduleExportResolverFactory cannot be null");
    }

    @Override
    public ScriptAccess getScriptAccess() {
        return scriptAccess;
    }

    @Override
    public void setScriptAccess(ScriptAccess scriptAccess) {
        this.scriptAccess = Objects.requireNonNull(scriptAccess, "scriptAccess cannot be null");
    }

    @Override
    public ScriptInternalGlobalModule getGlobalModule() {
        return globalModule;
    }

    @Override
    public Map<String, ScriptModule> getModules() {
        return Collections.unmodifiableMap(modules);
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

    public void applyTo(Context context, ScriptableObject scope) {
        RhinoModuleLoader loader = new RhinoModuleLoader(this, context, scope);
        ScriptableObject.putProperty(scope, REQUIRE_NAME, loader);

        try {
            ScriptModuleExportResolver resolver = moduleExportResolverFactory.create(new RhinoModuleContext(context, scope));
            for (ScriptExport export : globalModule.getExports()) {
                ScriptableObject.putProperty(scope, export.getName(), resolver.resolve(export));
            }
        } catch (ScriptModuleWrongContextException e) {
            throw new RuntimeException(e);
        }
    }
}
