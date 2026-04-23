package org.densy.scriptify.api.script.module.export.resolver;

import org.densy.scriptify.api.script.module.export.ScriptExport;

public interface ScriptModuleExportResolver {
    Object resolve(ScriptExport export);
}
