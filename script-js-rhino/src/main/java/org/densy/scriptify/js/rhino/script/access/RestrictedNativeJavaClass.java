package org.densy.scriptify.js.rhino.script.access;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class RestrictedNativeJavaClass extends NativeJavaClass {

    public RestrictedNativeJavaClass(Scriptable scope, Class<?> javaClass) {
        super(scope, javaClass);
    }

    @Override
    public boolean has(String name, Scriptable start) {
        Class<?> type = getClassObject();
        return RhinoScriptAccessSupport.findExportedField(type, name, true) != null
                || RhinoScriptAccessSupport.hasExportedMethod(type, name, true);
    }

    @Override
    public Object get(String name, Scriptable start) {
        Class<?> type = getClassObject();
        Field field = RhinoScriptAccessSupport.findExportedField(type, name, true);
        if (field != null) {
            try {
                return Context.getCurrentContext().getWrapFactory().wrap(
                        Context.getCurrentContext(),
                        getParentScope(),
                        field.get(null),
                        field.getType()
                );
            } catch (IllegalAccessException e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }

        if (RhinoScriptAccessSupport.hasExportedMethod(type, name, true)) {
            return new RestrictedJavaMethodFunction(getParentScope(), null, type, name, true);
        }

        return Scriptable.NOT_FOUND;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        Field field = RhinoScriptAccessSupport.findExportedField(getClassObject(), name, true);
        if (field == null) {
            throw Context.reportRuntimeError("Java static member is not exported: " + name);
        }
        try {
            field.set(null, Context.jsToJava(value, field.getType()));
        } catch (IllegalAccessException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    @Override
    public Object[] getIds() {
        return RhinoScriptAccessSupport.getExportedClassMemberNames(getClassObject()).toArray();
    }

    @Override
    public Scriptable construct(Context context, Scriptable scope, Object[] args) {
        for (Constructor<?> constructor : RhinoScriptAccessSupport.getExportedConstructors(getClassObject())) {
            Object[] converted = RhinoScriptAccessSupport.convertArguments(
                    context,
                    args,
                    constructor.getParameterTypes(),
                    constructor.isVarArgs()
            );
            if (converted == null) {
                continue;
            }

            try {
                Object instance = constructor.newInstance(converted);
                return context.getWrapFactory().wrapNewObject(context, scope, instance);
            } catch (IllegalArgumentException ignored) {
                // Try the next overload.
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }

        throw Context.reportRuntimeError("No exported constructor matches provided arguments for " + getClassObject().getName());
    }
}
