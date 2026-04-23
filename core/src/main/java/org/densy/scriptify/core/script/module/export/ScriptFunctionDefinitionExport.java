package org.densy.scriptify.core.script.module.export;

import lombok.Getter;
import org.densy.scriptify.api.script.function.definition.ScriptFunctionDefinition;
import org.densy.scriptify.api.script.module.export.ScriptExport;

@Getter
public final class ScriptFunctionDefinitionExport implements ScriptExport {

    private final ScriptFunctionDefinition definition;

    public ScriptFunctionDefinitionExport(ScriptFunctionDefinition definition) {
        this.definition = definition;
    }

    @Override
    public String getName() {
        return definition.getFunction().getName();
    }
}
