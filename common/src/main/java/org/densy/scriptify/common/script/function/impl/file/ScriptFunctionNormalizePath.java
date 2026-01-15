package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

public class ScriptFunctionNormalizePath implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "normalizePath";
    }

    @ExecuteAt
    public String execute(
            @Argument(name = "path") String path
    ) {
        return path.replace('\\', '/');
    }
}