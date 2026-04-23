package org.densy.scriptify.js.graalvm.script.module.export.resolver;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;
import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolverFactory;
import org.graalvm.polyglot.Context;

public final class GraalModuleExportResolverFactory implements ScriptModuleExportResolverFactory {

    private final Script<?> script;

    public GraalModuleExportResolverFactory(Script<?> script) {
        this.script = script;
    }

    @Override
    public ScriptModuleExportResolver create(Object context) throws ScriptModuleWrongContextException {
        if (!(context instanceof Context graalContext)) {
            throw new ScriptModuleWrongContextException(Context.class, context.getClass());
        }
        return new GraalModuleExportResolver(script, graalContext);
    }
}
