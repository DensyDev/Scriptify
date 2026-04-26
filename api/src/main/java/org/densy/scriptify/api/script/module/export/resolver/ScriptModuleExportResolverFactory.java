package org.densy.scriptify.api.script.module.export.resolver;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;

/**
 * A factory for creating an export resolver.
 */
public interface ScriptModuleExportResolverFactory {

    /**
     * Creates a resolver.
     *
     * @param context Engine context. Depends on the specific implementation.
     * @return created ScriptModuleExportResolver
     * @throws ScriptModuleWrongContextException if an incorrect context is passed to a specific implementation of the engine.
     */
    ScriptModuleExportResolver create(Object context) throws ScriptModuleWrongContextException;
}
