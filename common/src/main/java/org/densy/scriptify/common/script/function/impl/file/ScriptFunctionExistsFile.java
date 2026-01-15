package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;

/**
 * Represents a function to check the existence of a file
 */
public class ScriptFunctionExistsFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "existsFile";
    }

    @ExecuteAt
    public boolean execute(
            @Executor Script<?> script,
            @Argument(name = "filePath") String filePath
    ) {
        return Files.exists(script.getSecurityManager().getFileSystem().getPath(filePath));
    }
}
