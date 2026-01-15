package org.densy.scriptify.common.script.constant;

import org.densy.scriptify.core.script.constant.StandardConstantManager;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantBaseDir;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantOsName;

public class CommonConstantManager extends StandardConstantManager {

    public CommonConstantManager() {
        this.register(new ScriptConstantOsName());
        this.register(new ScriptConstantBaseDir());
    }
}
