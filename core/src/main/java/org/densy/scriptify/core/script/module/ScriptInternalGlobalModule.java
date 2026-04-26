package org.densy.scriptify.core.script.module;

import org.jetbrains.annotations.NotNull;

/**
 * A global internal module. All exports are available globally.
 */
public final class ScriptInternalGlobalModule extends AbstractScriptInternalModule {

    @Override
    public @NotNull String getName() {
        return "global";
    }
}
