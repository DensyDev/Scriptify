package org.densy.scriptify.core.script.module.export;

import lombok.Getter;
import org.densy.scriptify.api.script.constant.ScriptConstant;
import org.densy.scriptify.api.script.module.export.ScriptExport;

/**
 * A script module export for a constant.
 */
@Getter
public final class ScriptConstantExport implements ScriptExport {

    private final ScriptConstant constant;

    public ScriptConstantExport(ScriptConstant constant) {
        this.constant = constant;
    }

    @Override
    public String getName() {
        return constant.getName();
    }
}
