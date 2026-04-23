package org.densy.scriptify.api.script.module.export.resolver;

import org.densy.scriptify.api.exception.ScriptModuleWrongContextException;

public interface ScriptModuleExportResolverFactory {
    ScriptModuleExportResolver create(Object context) throws ScriptModuleWrongContextException;
}
