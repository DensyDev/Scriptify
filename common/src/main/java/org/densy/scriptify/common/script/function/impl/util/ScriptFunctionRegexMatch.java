package org.densy.scriptify.common.script.function.impl.util;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Represents a function to match regex pattern
 */
public class ScriptFunctionRegexMatch implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "regex_match";
    }

    @ExecuteAt
    public boolean execute(
            @Argument(name = "regex") String regex,
            @Argument(name = "value") String value
    ) {
        return Pattern.compile(regex).matcher(value).matches();
    }

    @ExecuteAt
    public boolean execute(
            @Argument(name = "pattern") Pattern pattern,
            @Argument(name = "value") String value
    ) {
        return pattern.matcher(value).matches();
    }
}
