package org.densy.scriptify.http.script.function.impl;

import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.http.script.function.data.HttpMethod;
import org.densy.scriptify.http.script.function.data.HttpRequest;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a function create http request
 */
public class ScriptFunctionCreateHttpRequest implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "createHttpRequest";
    }

    @ExecuteAt
    public HttpRequest execute(
            @Argument(name = "url") String url,
            @Argument(name = "method") Object rawMethod
    ) {
        if (rawMethod instanceof HttpMethod method) {
            return new HttpRequest(url, method);
        } else {
            return new HttpRequest(url, HttpMethod.valueOf(String.valueOf(rawMethod)));
        }
    }
}
