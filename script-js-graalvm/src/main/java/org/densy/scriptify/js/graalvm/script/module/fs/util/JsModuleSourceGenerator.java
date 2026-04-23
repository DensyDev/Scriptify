package org.densy.scriptify.js.graalvm.script.module.fs.util;

import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.graalvm.polyglot.Context;

import java.util.ArrayList;
import java.util.List;

public final class JsModuleSourceGenerator {

    public static final String BRIDGE_PREFIX = "__scriptify_bridge_";

    public static String generateModuleSource(Context context, ScriptModule module) {
        StringBuilder builder = new StringBuilder();
        builder.append("// @generated module: ").append(module.getName()).append("\n");

        List<String> names = new ArrayList<>();

        for (ScriptExport export : module.getExports()) {
            String name = export.getName();
            names.add(name);

            if (export instanceof ScriptValueExport valueExport) {
                if (valueExport.isClass()) {
                    Class<?> valueClass = (Class<?>) valueExport.getValue();
                    builder.append("const ").append(name)
                            .append(" = Java.type('").append(valueClass.getName()).append("');\n");
                } else {
                    String bridge = BRIDGE_PREFIX + name;
                    context.getBindings("js").putMember(bridge, context.asValue(valueExport.getValue()));
                    builder.append("const ").append(name)
                            .append(" = globalThis.").append(bridge).append(";\n");
                }
            }
        }

        builder.append("\nexport { ").append(String.join(", ", names)).append(" };\n");
        return builder.toString();
    }

    public static String encodeModuleName(String name) {
        return name.replace("@", "_at_").replace("/", "__");
    }

    public static String decodeModuleName(String encoded) {
        return encoded.replace("_at_", "@").replace("__", "/");
    }
}
