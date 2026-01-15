package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Represents a function to write the contents of a file
 */
public class ScriptFunctionWriteFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "writeFile";
    }

    @ExecuteAt
    public String execute(
            @Executor Script<?> script,
            @Argument(name = "filePath") String filePath,
            @Argument(name = "fileContent") String fileContent
    ) {
        try {
            return Files.writeString(script.getSecurityManager().getFileSystem().getPath(filePath), fileContent).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}
