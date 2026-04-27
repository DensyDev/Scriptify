package org.densy.scriptify.http.script.function.data;

import org.densy.scriptify.api.script.module.export.access.ScriptAccess;

public enum HttpMethod {
    @ScriptAccess.Export GET,
    @ScriptAccess.Export PUT,
    @ScriptAccess.Export POST,
    @ScriptAccess.Export DELETE,
    @ScriptAccess.Export PATCH,
    @ScriptAccess.Export HEAD,
    @ScriptAccess.Export OPTIONS,
    @ScriptAccess.Export TRACE
}
