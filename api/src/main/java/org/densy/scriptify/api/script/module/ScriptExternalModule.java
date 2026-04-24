package org.densy.scriptify.api.script.module;

import org.densy.scriptify.api.exception.ScriptModuleLoadException;

/**
 * A script module loaded from an external source.
 */
public interface ScriptExternalModule extends ScriptModule {

    /**
     * Loads the module source as bytes.
     */
    byte[] load() throws ScriptModuleLoadException;

    /**
     * Gets external module source name.
     */
    String getSourceName();
}
