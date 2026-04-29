package org.densy.scriptify.js.rhino.script.access;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class RestrictedJavaMethodFunction extends BaseFunction {

    private final Object target;
    private final Class<?> type;
    private final String name;
    private final boolean staticOnly;

    RestrictedJavaMethodFunction(Scriptable scope, Object target, Class<?> type, String name, boolean staticOnly) {
        this.target = target;
        this.type = type;
        this.name = name;
        this.staticOnly = staticOnly;
        this.setParentScope(scope);
        this.setPrototype(getFunctionPrototype(scope));
    }

    @Override
    public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args) {
        for (Method method : type.getMethods()) {
            if (!isCandidate(method)) {
                continue;
            }

            Object[] converted = RhinoScriptAccessSupport.convertArguments(
                    context,
                    args,
                    method.getParameterTypes(),
                    method.isVarArgs()
            );
            if (converted == null) {
                continue;
            }

            try {
                Object result = method.invoke(RhinoScriptAccessSupport.isStatic(method) ? null : target, converted);
                return context.getWrapFactory().wrap(context, scope, result, method.getReturnType());
            } catch (IllegalArgumentException ignored) {
                // Try the next overload.
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }

        throw Context.reportRuntimeError("No exported method '" + name + "' matches provided arguments");
    }

    private boolean isCandidate(Method method) {
        return method.getName().equals(name)
                && RhinoScriptAccessSupport.isExported(method)
                && (!staticOnly || RhinoScriptAccessSupport.isStatic(method))
                && (staticOnly || !RhinoScriptAccessSupport.isStatic(method));
    }
}
