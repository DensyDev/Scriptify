package org.densy.scriptify.core.script.module;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleScriptInternalModule extends AbstractScriptInternalModule {
    private final String name;

    public SimpleScriptInternalModule(String name) {
        this.name = Objects.requireNonNull(name, "Module name cannot be null");
    }

    @Override
    public @NotNull String getName() {
        return name;
    }
}