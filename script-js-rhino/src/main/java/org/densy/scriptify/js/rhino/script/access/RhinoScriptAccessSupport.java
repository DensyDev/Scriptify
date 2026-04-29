package org.densy.scriptify.js.rhino.script.access;

import org.densy.scriptify.api.script.module.export.access.ScriptAccess;
import org.mozilla.javascript.Context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RhinoScriptAccessSupport {

    private RhinoScriptAccessSupport() {}

    public static boolean isExplicit(ScriptAccess access) {
        return access == ScriptAccess.EXPLICIT;
    }

    static boolean isExported(AccessibleObject object) {
        return object.isAnnotationPresent(ScriptAccess.Export.class);
    }

    static Set<String> getExportedInstanceMemberNames(Class<?> type) {
        Set<String> names = new LinkedHashSet<>();
        for (Field field : type.getFields()) {
            if (isExported(field) && !Modifier.isStatic(field.getModifiers())) {
                names.add(field.getName());
            }
        }
        for (Method method : type.getMethods()) {
            if (isExported(method) && !Modifier.isStatic(method.getModifiers())) {
                names.add(method.getName());
            }
        }
        return names;
    }

    static Set<String> getExportedClassMemberNames(Class<?> type) {
        Set<String> names = new LinkedHashSet<>();
        for (Field field : type.getFields()) {
            if (isExported(field) && Modifier.isStatic(field.getModifiers())) {
                names.add(field.getName());
            }
        }
        for (Method method : type.getMethods()) {
            if (isExported(method) && Modifier.isStatic(method.getModifiers())) {
                names.add(method.getName());
            }
        }
        return names;
    }

    static Field findExportedField(Class<?> type, String name, boolean staticOnly) {
        for (Field field : type.getFields()) {
            if (field.getName().equals(name)
                    && isExported(field)
                    && (!staticOnly || Modifier.isStatic(field.getModifiers()))) {
                return field;
            }
        }
        return null;
    }

    static boolean hasExportedMethod(Class<?> type, String name, boolean staticOnly) {
        return Arrays.stream(type.getMethods())
                .anyMatch(method -> method.getName().equals(name)
                        && isExported(method)
                        && (!staticOnly || Modifier.isStatic(method.getModifiers()))
                        && (staticOnly || !Modifier.isStatic(method.getModifiers())));
    }

    static Constructor<?>[] getExportedConstructors(Class<?> type) {
        return Arrays.stream(type.getConstructors())
                .filter(RhinoScriptAccessSupport::isExported)
                .toArray(Constructor<?>[]::new);
    }

    static Object[] convertArguments(Context context, Object[] args, Class<?>[] parameterTypes, boolean varArgs) {
        if (!varArgs) {
            if (args.length != parameterTypes.length) {
                return null;
            }
            Object[] converted = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                converted[i] = convertArgument(context, args[i], parameterTypes[i]);
            }
            return converted;
        }

        int fixedCount = parameterTypes.length - 1;
        if (args.length < fixedCount) {
            return null;
        }

        Object[] converted = new Object[parameterTypes.length];
        for (int i = 0; i < fixedCount; i++) {
            converted[i] = convertArgument(context, args[i], parameterTypes[i]);
        }

        Class<?> componentType = parameterTypes[fixedCount].getComponentType();
        Object varArgArray = java.lang.reflect.Array.newInstance(componentType, args.length - fixedCount);
        for (int i = fixedCount; i < args.length; i++) {
            java.lang.reflect.Array.set(varArgArray, i - fixedCount, convertArgument(context, args[i], componentType));
        }
        converted[fixedCount] = varArgArray;
        return converted;
    }

    static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    private static Object convertArgument(Context context, Object value, Class<?> targetType) {
        return Context.jsToJava(value, targetType);
    }
}
