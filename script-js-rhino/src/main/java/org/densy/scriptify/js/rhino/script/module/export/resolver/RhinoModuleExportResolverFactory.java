package org.densy.scriptify.js.rhino.script.module.export.resolver;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolverFactory;
import org.densy.scriptify.js.rhino.script.module.RhinoModuleContext;

public final class RhinoModuleExportResolverFactory implements ScriptModuleExportResolverFactory {

    private final Script<?> script;

    public RhinoModuleExportResolverFactory(Script<?> script) {
        this.script = script;
    }

    @Override
    public ScriptModuleExportResolver create(Object context) throws ScriptModuleWrongContextException {
        if (!(context instanceof RhinoModuleContext rhinoContext)) {
            throw new ScriptModuleWrongContextException(RhinoModuleContext.class, context.getClass());
        }
        return new RhinoModuleExportResolver(script, rhinoContext.context(), rhinoContext.scope());
    }
}
