package org.densy.scriptify.api.script.module;

import org.densy.scriptify.api.script.module.export.access.ScriptAccess;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolverFactory;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

/**
 * Manages all modules available to the script.
 */
public interface ScriptModuleManager {

    /**
     * Gets a factory for module export values.
     *
     * @return ScriptModuleExportResolverFactory
     */
    ScriptModuleExportResolverFactory getModuleExportResolver();

    /**
     * Sets a custom factory for module export values.
     *
     * @param factory ScriptModuleExportResolverFactory to set
     */
    void setModuleExportResolver(ScriptModuleExportResolverFactory factory);

    /**
     * Gets the script's access to the module and its exported properties.
     *
     * @return ScriptAccess enum
     */
    ScriptAccess getScriptAccess();

    /**
     * Sets the script's access to the module and its exported properties.
     *
     * @param scriptAccess ScriptAccess to set
     */
    void setScriptAccess(ScriptAccess scriptAccess);

    /**
     * Gets the internal global module. Exports added here are available globally without import.
     */
    ScriptInternalModule getGlobalModule();

    /**
     * Gets all registered modules.
     *
     * @return Map<String, ScriptModule>
     */
    @UnmodifiableView Map<String, ScriptModule> getModules();

    /**
     * Gets registered module by name.
     *
     * @param name the module name
     * @return found module or null
     */
    default @Nullable ScriptModule getModule(String name) {
        return this.getModules().get(name);
    }

    /**
     * Adds module to the script.
     *
     * @param module module to add
     */
    void addModule(ScriptModule module);

    /**
     * Removes registered module from the script.
     *
     * @param name the name of module to remove
     */
    void removeModule(String name);
}