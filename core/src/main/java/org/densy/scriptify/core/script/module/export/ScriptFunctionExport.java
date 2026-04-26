package org.densy.scriptify.core.script.module.export;

import lombok.Getter;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.module.export.ScriptExport;

/**
 * A script module export for a function.
 */
@Getter
public final class ScriptFunctionExport implements ScriptExport {

    private final ScriptFunction function;

    public ScriptFunctionExport(ScriptFunction function) {
        this.function = function;
    }

    @Override
    public String getName() {
        return function.getName();
    }
}
