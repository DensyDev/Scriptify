package org.densy.scriptify.common.script.function.impl.crypto;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Represents a function to decode base64 to string
 */
public class ScriptFunctionBase64Decode implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "base64decode";
    }

    @ExecuteAt
    public String execute(
            @Argument(name = "string") String string
    ) {
        return new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
    }
}
