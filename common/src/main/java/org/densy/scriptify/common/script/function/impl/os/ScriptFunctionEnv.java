package org.densy.scriptify.common.script.function.impl.os;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a function to get environment variable value
 */
public class ScriptFunctionEnv implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "env";
    }

    @ExecuteAt
    public String execute(
            @Argument(name = "name") String name
    ) {
        return System.getenv(name);
    }
}
