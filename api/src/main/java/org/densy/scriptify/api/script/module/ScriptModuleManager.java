package org.densy.scriptify.api.script.module;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

/**
 * Manages all modules available to the script.
 * The global module is always present and created automatically.
 */
public interface ScriptModuleManager {

    /**
     * Exports added here are available globally without import
     */
    ScriptModule getGlobalModule();

    @UnmodifiableView Map<String, ScriptModule> getModules();

    default @Nullable ScriptModule getModule(String name) {
        return this.getModules().get(name);
    }

    void addModule(ScriptModule module);

    void removeModule(String name);
}