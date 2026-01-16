package org.densy.scriptify.core.script.constant.impl;

import org.densy.scriptify.api.script.constant.ScriptConstant;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

/**
 * Represents a constant with base dir
 */
public class ScriptConstantBaseDir implements ScriptConstant {

    @Override
    public @NotNull String getName() {
        return "baseDir";
    }

    @Override
    public Object getValue() {
        return Paths.get("").toAbsolutePath().toString();
    }
}
