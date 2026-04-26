package org.densy.scriptify.http.script.module;

import org.densy.scriptify.api.script.module.export.ScriptValueExport;
import org.densy.scriptify.core.script.module.AbstractScriptInternalModule;
import org.densy.scriptify.core.script.module.export.ScriptFunctionExport;
import org.densy.scriptify.http.script.function.data.HttpMethod;
import org.densy.scriptify.http.script.function.data.HttpRequest;
import org.densy.scriptify.http.script.function.data.OutputType;
import org.densy.scriptify.http.script.function.impl.ScriptFunctionCreateHttpRequest;
import org.jetbrains.annotations.NotNull;

public class HttpScriptModule extends AbstractScriptInternalModule {

    public HttpScriptModule() {
        this.export(new ScriptValueExport("HttpRequest", HttpRequest.class));
        this.export(new ScriptValueExport("HttpMethod", HttpMethod.class));
        this.export(new ScriptValueExport("OutputType", OutputType.class));
        this.export(new ScriptFunctionExport(new ScriptFunctionCreateHttpRequest()));
    }

    @Override
    public @NotNull String getName() {
        return "http";
    }
}
