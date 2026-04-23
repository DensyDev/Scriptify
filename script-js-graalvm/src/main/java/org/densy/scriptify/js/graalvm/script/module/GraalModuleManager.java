package org.densy.scriptify.js.graalvm.script.module;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.ScriptModuleManager;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolverFactory;
import org.densy.scriptify.core.script.module.ScriptGlobalModule;
import org.densy.scriptify.core.script.module.export.ScriptConstantExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.densy.scriptify.js.graalvm.script.JsFunction;
import org.densy.scriptify.js.graalvm.script.module.export.resolver.GraalModuleExportResolverFactory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GraalModuleManager implements ScriptModuleManager {

    private final Script<?> script;
    private final ScriptGlobalModule globalModule = new ScriptGlobalModule();
    private final Map<String, ScriptModule> modules = new LinkedHashMap<>();
    private ScriptModuleExportResolverFactory moduleExportResolverFactory;

    public GraalModuleManager(Script<?> script) {
        this.script = script;
        this.setModuleExportResolver(new GraalModuleExportResolverFactory(script));
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
    public ScriptGlobalModule getGlobalModule() {
        return globalModule;
    }

    @Override
    public Map<String, ScriptModule> getModules() {
        return modules;
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

        try {
            ScriptModuleExportResolver resolver = moduleExportResolverFactory.create(context);
            for (ScriptExport export : globalModule.getExports()) {
                bindings.putMember(export.getName(), resolver.resolve(export));
            }
        } catch (ScriptModuleWrongContextException e) {
            throw new RuntimeException(e);
        }
    }
}
