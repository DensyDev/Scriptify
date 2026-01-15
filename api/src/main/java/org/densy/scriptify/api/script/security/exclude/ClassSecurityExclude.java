package org.densy.scriptify.api.script.security.exclude;

/**
 * Excludes a specific class for security purposes.
 */
public class ClassSecurityExclude implements SecurityExclude {

    private final Class<?> value;

    /**
     * Creates a class exclusion rule.
     *
     * @param value Class to exclude
     */
    public ClassSecurityExclude(Class<?> value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value.getName();
    }
}
