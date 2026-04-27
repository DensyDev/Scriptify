package org.densy.scriptify.api.script.module.export.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the script's access to the module and its exported properties.
 */
public enum ScriptAccess {
    /**
     * Full access.
     */
    ALL,
    /**
     * Explicit access to fields, methods, and constructors marked with an annotation {@link ScriptAccess.Export}
     */
    EXPLICIT;

    /**
     * An annotation that exports access to a method, field, or constructor for a script.
     *
     * <pre>
     * public class MyService {
     *     public String publicMethod() { ... }      // unavailable in script
     *
     *     @ScriptAccess.Export
     *     public String internalMethod() { ... }    // available in script
     *
     *     @ScriptAccess.Export
     *     public String sensitiveField;             // available in script
     * }
     * </pre>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
    public @interface Export {

    }
}