package org.densy.scriptify.core.script.module;

import org.densy.scriptify.api.exception.ScriptModuleLoadException;
import org.densy.scriptify.api.script.module.ScriptStreamExternalModule;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Stream-based external module. Source supplier is called on every load() - allows dynamic reloading.
 */
public class SimpleScriptStreamExternalModule implements ScriptStreamExternalModule {

    private final String name;
    private final String sourceName;
    private final Supplier<byte[]> sourceSupplier;

    public SimpleScriptStreamExternalModule(String name, String sourceName, byte[] source) {
        this(name, sourceName, () -> source);
    }

    public SimpleScriptStreamExternalModule(String name, String sourceName, Supplier<byte[]> sourceSupplier) {
        this.name = Objects.requireNonNull(name);
        this.sourceName = Objects.requireNonNull(sourceName);
        this.sourceSupplier = Objects.requireNonNull(sourceSupplier);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public byte[] load() throws ScriptModuleLoadException {
        try {
            return sourceSupplier.get();
        } catch (Exception e) {
            throw new ScriptModuleLoadException("Failed to load stream module " + name, e);
        }
    }
}