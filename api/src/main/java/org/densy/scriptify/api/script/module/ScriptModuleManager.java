package org.densy.scriptify.api.script.module;

/**
 * Manages all modules available to the script.
 * The global module is always present and created automatically.
 */
public interface ScriptModuleManager {

    /**
     * Exports added here are available globally without import
     */
    ScriptModule getGlobalModule();

    ScriptModule getModule(String name);

    void addModule(ScriptModule module);

    void removeModule(String name);
}