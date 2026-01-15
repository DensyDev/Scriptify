package org.densy.scriptify.api.script.constant;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a constant that can be used within scripts.
 */
public interface ScriptConstant {

    /**
     * Gets the name of the constant.
     *
     * @return The name of the constant
     */
    @NotNull String getName();

    /**
     * Gets the value of the constant.
     *
     * @return The value of the constant
     */
    @Nullable Object getValue();

    /**
     * Creates a new ScriptConstant instance with the given name and value.
     *
     * @param name The name of the constant
     * @param value The value of the constant
     * @return A new ScriptConstant instance
     */
    static ScriptConstant of(String name, Object value) {
        return new ScriptConstant() {
            @Override
            public @NotNull String getName() {
                return name;
            }

            @Override
            public Object getValue() {
                return value;
            }
        };
    }
}
