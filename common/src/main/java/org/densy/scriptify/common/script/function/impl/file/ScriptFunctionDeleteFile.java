package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents a function to delete a file in the normal or recursive way
 */
public class ScriptFunctionDeleteFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "deleteFile";
    }

    @ExecuteAt
    public boolean execute(
            @Executor Script<?> script,
            @Argument(name = "filePath") String filePath,
            @Argument(name = "recursive", required = false) Boolean recursive
    ) {
        File file = script.getSecurityManager().getFileSystem().getFile(filePath);
        if (recursive != null && recursive) {
            return deleteDirectoryRecursively(file);
        } else {
            return file.delete();
        }
    }

    private boolean deleteDirectoryRecursively(File file) {
        boolean success = true;

        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    if (!deleteDirectoryRecursively(entry)) {
                        success = false;
                    }
                }
            }
        }

        if (!file.delete()) {
            success = false;
        }

        return success;
    }
}
