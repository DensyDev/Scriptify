package org.densy.scriptify.core.script.module;

import org.jetbrains.annotations.NotNull;

public final class ScriptInternalGlobalModule extends AbstractScriptInternalModule {

    @Override
    public @NotNull String getName() {
        return "global";
    }
}
