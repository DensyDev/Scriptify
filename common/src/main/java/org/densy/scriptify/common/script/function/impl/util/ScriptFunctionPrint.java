package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a function for outputting messages to the console
 */
public class ScriptFunctionPrint implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "print";
    }

    @ExecuteAt
    public void execute(
            @Argument(name = "args") Object... args
    ) {
        System.out.println(Arrays.stream(args).map(Object::toString).collect(Collectors.joining(" ")));
    }
}
