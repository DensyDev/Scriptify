package org.densy.scriptify.api.script.security.exclude;

/**
 * Excludes an entire package for security purposes.
 */
public class PackageSecurityExclude implements SecurityExclude {

    private final String value;

    /**
     * Creates a package exclusion rule.
     *
     * @param value Package name to exclude
     */
    public PackageSecurityExclude(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
