package org.densy.scriptify.core.script.module;

import org.densy.scriptify.api.exception.ScriptModuleCopyException;
import org.densy.scriptify.api.script.module.ScriptInternalModule;
import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An abstract class for an internal module.
 */
public abstract class AbstractScriptInternalModule implements ScriptInternalModule {
    private final Map<String, ScriptExport> exports = new LinkedHashMap<>();

    @Override
    public void export(ScriptExport export) {
        if (export == null) {
            throw new IllegalArgumentException("Export cannot be null");
        }
        exports.put(export.getName(), export);
    }

    @Override
    public void copy(ScriptInternalModule module) {
        // We need to verify that the module from which we want to copy exports
        // does not contain any exports with the same name as in the current
        // module but with a different hash.
        boolean conflicts = module.getExports().stream().anyMatch(e ->
                exports.values().stream().anyMatch(existing ->
                        existing.getName().equals(e.getName()) &&
                                existing.hashCode() != e.hashCode()
                )
        );

        if (conflicts) {
            throw new ScriptModuleCopyException("The copy operation cannot be performed: both modules contain different exports with the same name");
        }

        module.getExports().forEach(this::export);
    }

    @Override
    public @UnmodifiableView Collection<ScriptExport> getExports() {
        return Collections.unmodifiableCollection(exports.values());
    }
}