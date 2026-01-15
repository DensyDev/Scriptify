package org.densy.scriptify.common.script.function.impl.os;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Represents a function to execute command
 */
public class ScriptFunctionExecCommand implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "execCommand";
    }

    @ExecuteAt
    public String execute(
            @Argument(name = "input") String input
    ) {
        try {
            Process process = Runtime.getRuntime().exec(input);

            StringBuilder message = new StringBuilder();
            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = stdInput.readLine()) != null) {
                    message.append(line).append("\n");
                }
                while ((line = stdError.readLine()) != null) {
                    message.append(line).append("\n");
                }
            }
            return message.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
