package org.densy.scriptify.js.rhino.script.access;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Field;

public final class RestrictedNativeJavaObject extends NativeJavaObject {

    public RestrictedNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
        super(scope, javaObject, staticType);
    }

    @Override
    public boolean has(String name, Scriptable start) {
        Class<?> type = getWrappedType();
        return RhinoScriptAccessSupport.findExportedField(type, name, false) != null
                || RhinoScriptAccessSupport.hasExportedMethod(type, name, false);
    }

    @Override
    public Object get(String name, Scriptable start) {
        Class<?> type = getWrappedType();
        Field field = RhinoScriptAccessSupport.findExportedField(type, name, false);
        if (field != null) {
            try {
                Object value = field.get(unwrap());
                return Context.getCurrentContext().getWrapFactory().wrap(
                        Context.getCurrentContext(),
                        getParentScope(),
                        value,
                        field.getType()
                );
            } catch (IllegalAccessException e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }

        if (RhinoScriptAccessSupport.hasExportedMethod(type, name, false)) {
            return new RestrictedJavaMethodFunction(getParentScope(), unwrap(), type, name, false);
        }

        return Scriptable.NOT_FOUND;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        Field field = RhinoScriptAccessSupport.findExportedField(getWrappedType(), name, false);
        if (field == null) {
            throw Context.reportRuntimeError("Java member is not exported: " + name);
        }
        try {
            field.set(unwrap(), Context.jsToJava(value, field.getType()));
        } catch (IllegalAccessException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    @Override
    public Object[] getIds() {
        return RhinoScriptAccessSupport.getExportedInstanceMemberNames(getWrappedType()).toArray();
    }

    private Class<?> getWrappedType() {
        Object wrapped = unwrap();
        if (staticType != null && staticType != Object.class) {
            return staticType;
        }
        return wrapped.getClass();
    }
}
