package org.densy.scriptify.js.rhino.script.module.export.resolver;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.densy.scriptify.core.script.module.export.ScriptConstantExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionDefinitionExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.densy.scriptify.core.script.module.export.resolver.MappedModuleExportResolver;
import org.densy.scriptify.js.rhino.script.JsFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.ScriptableObject;

public final class RhinoModuleExportResolver extends MappedModuleExportResolver {

    public RhinoModuleExportResolver(Script<?> script, ScriptableObject scope) {
        this.mapping(ScriptValueExport.class, export -> {
            if (export.isClass()) {
                return new NativeJavaClass(scope, (Class<?>) export.getValue());
            }
            return Context.javaToJS(export.getValue(), scope);
        });
        this.mapping(ScriptFunctionExport.class, export -> new JsFunction(script, script.getFunctionManager()
                .getFunctionDefinitionFactory()
                .create(export.getFunction())
        ));
        this.mapping(ScriptFunctionDefinitionExport.class, export -> new JsFunction(script, export.getDefinition()));
        this.mapping(ScriptConstantExport.class, export -> Context.javaToJS(export.getConstant().getValue(), scope));
    }
}
