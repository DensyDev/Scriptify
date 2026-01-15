package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a function to get all files in a folder
 */
public class ScriptFunctionListFiles implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "listFiles";
    }

    @ExecuteAt
    public List<String> execute(
            @Executor Script<?> script,
            @Argument(name = "filePath") String filePath
    ) {
        File folder = script.getSecurityManager().getFileSystem().getFile(filePath);
        if (folder.isDirectory()) {
            return Arrays.stream(Objects.requireNonNull(folder.listFiles())).map(File::getAbsolutePath).toList();
        } else {
            throw new RuntimeException("File is not a folder");
        }
    }
}