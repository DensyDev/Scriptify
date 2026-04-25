package org.densy.scriptify.common.script.constant;

import org.densy.scriptify.core.script.constant.StandardConstantManager;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantBaseDir;
import org.densy.scriptify.core.script.constant.impl.ScriptConstantOsName;

/**
 * @deprecated this class is marked as deprecated in version 1.6. Modules have replaced managers.
 */
@Deprecated(forRemoval = true)
public class CommonConstantManager extends StandardConstantManager {

    public CommonConstantManager() {
        this.register(new ScriptConstantOsName());
        this.register(new ScriptConstantBaseDir());
    }
}
