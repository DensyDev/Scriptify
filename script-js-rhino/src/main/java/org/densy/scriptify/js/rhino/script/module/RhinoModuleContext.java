package org.densy.scriptify.js.rhino.script.module;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

public record RhinoModuleContext(Context context, ScriptableObject scope) {
}
