package org.densy.scriptify.core.script.module;

import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.densy.scriptify.api.script.module.ScriptModule;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractScriptModule implements ScriptModule {
    private final Map<String, ScriptExport> exports = new LinkedHashMap<>();

    @Override
    public void export(ScriptExport export) {
        if (export == null) {
            throw new IllegalArgumentException("Export cannot be null");
        }
        exports.put(export.getName(), export);
    }

    @Override
    public Collection<ScriptExport> getExports() {
        return Collections.unmodifiableCollection(exports.values());
    }
}