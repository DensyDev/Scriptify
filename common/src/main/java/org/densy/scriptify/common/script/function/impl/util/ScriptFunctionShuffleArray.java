package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a function to shuffle an array
 */
public class ScriptFunctionShuffleArray implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "shuffleArray";
    }

    @ExecuteAt
    public List<?> execute(
            @Argument(name = "array") List<?> array
    ) {
        List<?> list = new ArrayList<>(array);
        Collections.shuffle(list);
        return list;
    }
}
