package org.densy.scriptify.core.script.constant;

import org.densy.scriptify.api.script.constant.ScriptConstant;
import org.densy.scriptify.api.script.constant.ScriptConstantManager;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StandardConstantManager implements ScriptConstantManager {

    private final Map<String, ScriptConstant> constants = new HashMap<>();

    @Override
    public @UnmodifiableView Map<String, ScriptConstant> getConstants() {
        return Collections.unmodifiableMap(constants);
    }

    @Override
    public void register(ScriptConstant constant) {
        Objects.requireNonNull(constant, "constant cannot be null");
        if (!constants.containsKey(constant.getName())) {
            constants.put(constant.getName(), constant);
        } else {
            throw new IllegalStateException("The constant with this name already exists");
        }
    }

    @Override
    public void remove(String name) {
        if (constants.containsKey(name)) {
            constants.remove(name);
        } else {
            throw new IllegalArgumentException("The constant with this name does not exist");
        }
    }
}