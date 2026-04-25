package org.densy.scriptify.core.script.module.export.resolver;

import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class MappedModuleExportResolver implements ScriptModuleExportResolver {

    private final Map<Class<?>, Function<ScriptExport, Object>> resolvers = new HashMap<>();

    public <E extends ScriptExport> void mapping(Class<E> type, Function<E, Object> resolver) {
        resolvers.put(type, export -> resolver.apply(type.cast(export)));
    }

    @Override
    public Object resolve(ScriptExport export) {
        Function<ScriptExport, Object> resolver = resolvers.get(export.getClass());
        if (resolver == null) {
            throw new UnsupportedOperationException("No resolver found for export " + export.getClass());
        }
        return resolver.apply(export);
    }
}
