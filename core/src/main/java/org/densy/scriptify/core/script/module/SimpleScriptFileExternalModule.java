package org.densy.scriptify.core.script.module;

import org.densy.scriptify.api.exception.ScriptModuleLoadException;
import org.densy.scriptify.api.script.module.ScriptFileExternalModule;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class SimpleScriptFileExternalModule implements ScriptFileExternalModule {

    private final String name;
    private final Path path;
    private final String entryPoint;

    public SimpleScriptFileExternalModule(String name, Path path) {
        this(name, path, "index.mjs");
    }

    public SimpleScriptFileExternalModule(String name, Path path, String entryPoint) {
        this.name = Objects.requireNonNull(name);
        this.path = Objects.requireNonNull(path);
        this.entryPoint = Objects.requireNonNull(entryPoint);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public String getSourceName() {
        return path.getFileName().toString();
    }

    @Override
    public byte[] load() throws ScriptModuleLoadException {
        Path target = Files.isDirectory(path) ? path.resolve(entryPoint) : path;
        try {
            return Files.readAllBytes(target);
        } catch (IOException e) {
            throw new ScriptModuleLoadException("Failed to load external module " + name + "' from: " + target, e);
        }
    }
}