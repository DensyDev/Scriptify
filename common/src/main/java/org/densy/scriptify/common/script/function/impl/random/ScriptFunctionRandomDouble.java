package org.densy.scriptify.common.script.function.impl.random;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Represents a function to generate random double number
 */
public class ScriptFunctionRandomDouble implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "randomDouble";
    }

    @ExecuteAt
    public double execute(
            @Argument(name = "max") Double max
    ) {
        return new Random().nextDouble(max);
    }

    @ExecuteAt
    public double execute(
            @Argument(name = "min") Double min,
            @Argument(name = "max") Double max
    ) {
        return new Random().nextDouble(min, max);
    }
}
