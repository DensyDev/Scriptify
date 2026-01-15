package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a function to join path
 */
public class ScriptFunctionJoinPath implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "joinPath";
    }

    @ExecuteAt
    public String execute(
            @Argument(name = "args") String... args
    ) {
        StringBuilder path = new StringBuilder();
        for (String arg : args) {
            if (path.isEmpty()) {
                path.append(arg);
            } else {
                path.append('/').append(arg);
            }
        }
        return path.toString();
    }
}
