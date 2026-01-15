package org.densy.scriptify.api.script;

/**
 * Represents a class to wrap an object in a script,
 * so if this class is found in a script, it will be converted to its value.
 * This is necessary for script runtimes that do not support automatic object conversion.
 */
public interface ScriptObject {

    /**
     * Returns the stored value.
     *
     * @return The value held by this object
     */
    Object getValue();

    /**
     * Creates a ScriptObject from the given value.
     *
     * @param value The value to encapsulate
     * @return A new ScriptObject instance
     */
    static ScriptObject of(Object value) {
        return () -> value;
    }
}