package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents a function to create an array from the passed arguments
 */
public class ScriptFunctionArrayOf implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "arrayOf";
    }

    @ExecuteAt
    public Object[] execute(
            @Argument(name = "args") Object... args
    ) {
        return Arrays.stream(args).toArray();
    }
}
