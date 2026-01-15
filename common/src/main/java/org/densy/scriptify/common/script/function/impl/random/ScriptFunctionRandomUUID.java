package org.densy.scriptify.common.script.function.impl.random;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a function to generate random UUID
 */
public class ScriptFunctionRandomUUID implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "randomUUID";
    }

    @ExecuteAt
    public String execute() {
        return UUID.randomUUID().toString();
    }
}
