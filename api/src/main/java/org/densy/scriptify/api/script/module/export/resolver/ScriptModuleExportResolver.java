package org.densy.scriptify.api.script.module.export.resolver;

import org.densy.scriptify.api.script.module.export.ScriptExport;

/**
 * A value resolver for export. Used to determine which value in the script will be exported.
 */
public interface ScriptModuleExportResolver {

    /**
     * Resolves the value of export.
     *
     * @param export target export
     * @return resolved value
     */
    Object resolve(ScriptExport export);
}
