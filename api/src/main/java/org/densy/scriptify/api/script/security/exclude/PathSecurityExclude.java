package org.densy.scriptify.api.script.security.exclude;

/**
 * Excludes a specific path for security purposes.
 */
public class PathSecurityExclude implements SecurityExclude {

    private final String value;

    /**
     * Creates a path exclusion rule.
     *
     * @param value Path to exclude
     */
    public PathSecurityExclude(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
