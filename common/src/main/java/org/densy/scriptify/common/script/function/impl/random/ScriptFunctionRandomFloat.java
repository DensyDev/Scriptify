package org.densy.scriptify.common.script.function.impl.random;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Represents a function to generate random float number
 */
public class ScriptFunctionRandomFloat implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "randomFloat";
    }

    @ExecuteAt
    public float execute(
            @Argument(name = "max") Float max
    ) {
        return new Random().nextFloat(max);
    }

    @ExecuteAt
    public float execute(
            @Argument(name = "min") Float min,
            @Argument(name = "max") Float max
    ) {
        return new Random().nextFloat(min, max);
    }
}
