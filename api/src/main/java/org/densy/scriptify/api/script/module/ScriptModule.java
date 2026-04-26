package org.densy.scriptify.api.script.module;

import org.jetbrains.annotations.NotNull;

/**
 * A base interface for all module types.
 */
public interface ScriptModule {

    /**
     * Gets module name. For ES import "@densy/mymodule".
     */
    @NotNull String getName();
}