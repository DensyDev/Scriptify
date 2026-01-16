package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a function to create a set from the passed arguments
 */
public class ScriptFunctionSetOf implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "setOf";
    }

    @ExecuteAt
    public Set<?> execute(
            @Argument(name = "args") Object... args
    ) {
        return new HashSet<>(Arrays.asList(args));
    }
}
