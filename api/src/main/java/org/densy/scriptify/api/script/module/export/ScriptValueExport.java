package org.densy.scriptify.api.script.module.export;

import lombok.Getter;

/**
 * Universal wrapper for exporting Java values and classes.
 *
 * <pre>
 *   new ScriptValueExport("PI", 3.14)               - PI available as a number
 *   new ScriptValueExport("MyClass", MyClass.class) - new MyClass() in JS
 *   new ScriptValueExport("service", myService)     - access to instance methods
 * </pre>
 */
@Getter
public class ScriptValueExport implements ScriptExport {

    private final String name;
    private final Object value;

    public ScriptValueExport(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isClass() {
        return value instanceof Class<?>;
    }
}
