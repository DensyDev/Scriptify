package org.densy.scriptify.api.script.module;

import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

/**
 * A module that exports elements to the script environment.
 * Named modules (non-null name) are accessible via ES import.
 * The global module (null name) injects exports directly into global scope.
 */
public interface ScriptModule {

    /**
     * Module name for ES import, e.g. "@densy/mymodule".
     */
    @NotNull
    String getName();

    @UnmodifiableView
    Collection<ScriptExport> getExports();

    void export(ScriptExport export);
}