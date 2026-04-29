package org.densy.scriptify.js.rhino.script;

import org.densy.scriptify.api.script.ScriptObject;
import org.densy.scriptify.api.script.module.export.access.ScriptAccess;
import org.densy.scriptify.js.rhino.script.access.RestrictedNativeJavaClass;
import org.densy.scriptify.js.rhino.script.access.RestrictedNativeJavaObject;
import org.densy.scriptify.js.rhino.script.access.RhinoScriptAccessSupport;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class JsWrapFactory extends WrapFactory {

    private final ScriptAccess scriptAccess;

    public JsWrapFactory(ScriptAccess scriptAccess) {
        this.scriptAccess = scriptAccess;
        this.setJavaPrimitiveWrap(false);
    }

    @Override
    public Object wrap(Context context, Scriptable scope, Object object, Class<?> staticType) {
        // Convert the ScriptObject class to the value it contains
        if (object instanceof ScriptObject scriptObject) {
            return super.wrap(context, scope, scriptObject.getValue(), staticType);
        }
        return super.wrap(context, scope, object, staticType);
    }

    @Override
    public Scriptable wrapAsJavaObject(Context context, Scriptable scope, Object javaObject, Class<?> staticType) {
        if (RhinoScriptAccessSupport.isExplicit(scriptAccess)) {
            return new RestrictedNativeJavaObject(scope, javaObject, staticType);
        }
        return super.wrapAsJavaObject(context, scope, javaObject, staticType);
    }

    @Override
    public Scriptable wrapJavaClass(Context context, Scriptable scope, Class<?> javaClass) {
        if (RhinoScriptAccessSupport.isExplicit(scriptAccess)) {
            return new RestrictedNativeJavaClass(scope, javaClass);
        }
        return super.wrapJavaClass(context, scope, javaClass);
    }
}
