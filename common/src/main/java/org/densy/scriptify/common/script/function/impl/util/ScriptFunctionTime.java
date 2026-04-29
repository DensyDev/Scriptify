package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;


public class ScriptFunctionTime implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "time";
    }

    @ExecuteAt
    public long execute() {
        return Instant.now().toEpochMilli();
    }
}
