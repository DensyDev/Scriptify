package org.densy.scriptify.api.script.module;

import org.densy.scriptify.api.script.module.export.ScriptExport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

/**
 * A module that exports elements to the script environment. Modules can be accessed via ES import (in JavaScript).
 */
public interface ScriptModule {

    /**
     * Module name for ES import, e.g. "@densy/mymodule".
     */
    @NotNull String getName();

    /**
     * Gets collection of all exports in module.
     *
     * @return Collection with ScriptExport
     */
    @UnmodifiableView Collection<ScriptExport> getExports();

    /**
     * Adds export to the module.
     *
     * @param export export to add
     */
    void export(ScriptExport export);

    /**
     * Copies all exports from the target module that are not present in the current module.
     *
     * @param module target module
     */
    void copy(ScriptModule module);
}