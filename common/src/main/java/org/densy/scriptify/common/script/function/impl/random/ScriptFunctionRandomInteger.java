package org.densy.scriptify.common.script.function.impl.random;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Represents a function to generate random integer number
 */
public class ScriptFunctionRandomInteger implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "randomInt";
    }

    @ExecuteAt
    public int execute(
            @Argument(name = "max") Integer max
    ) {
        return new Random().nextInt(max);
    }

    @ExecuteAt
    public int execute(
            @Argument(name = "min") Integer min,
            @Argument(name = "max") Integer max
    ) {
        return new Random().nextInt(min, max);
    }
}
