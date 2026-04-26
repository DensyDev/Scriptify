package org.densy.scriptify.js.graalvm.script.module.export.resolver;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.densy.scriptify.core.script.module.export.ScriptConstantExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionDefinitionExport;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.densy.scriptify.core.script.module.export.resolver.MappedModuleExportResolver;
import org.densy.scriptify.js.graalvm.script.JsFunction;
import org.graalvm.polyglot.Context;

public final class GraalModuleExportResolver extends MappedModuleExportResolver {

    public GraalModuleExportResolver(Script<?> script, Context context) {
        this.mapping(ScriptValueExport.class, export -> context.asValue(export.getValue()));
        this.mapping(ScriptFunctionExport.class, export -> new JsFunction(script, script.getFunctionManager()
                .getFunctionDefinitionFactory()
                .create(export.getFunction())
        ));
        this.mapping(ScriptFunctionDefinitionExport.class, export -> new JsFunction(script, export.getDefinition()));
        this.mapping(ScriptConstantExport.class, export -> export.getConstant().getValue());
    }
}
