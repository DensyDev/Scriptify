package org.densy.scriptify.api.script.security.exclude;

/**
 * Defines exclusions for security purposes, specifically for paths, packages, or classes.
 * This interface acts as a base for creating exclusion rules in a security context.
 */
public interface SecurityExclude {

    /**
     * Value to be added to the exclusion.
     *
     * @return The exclusion value
     */
    String getValue();

    /**
     * Checks that the value of path or packet is excluded.
     *
     * @param value Path or package
     * @return True if excluded, otherwise false
     */
    default boolean isExcluded(String value) {
        // Check that the path starts from the path specified in the exclusion
        return value.startsWith(this.getValue());
    }

    /**
     * Creates a new exclusion instance for the package.
     *
     * @param value A package that will be excluded
     * @return A new exclusion instance for the package
     */
    static PackageSecurityExclude ofPackage(String value) {
        return new PackageSecurityExclude(value);
    }

    /**
     * Creates a new exclusion instance for the class.
     *
     * @param value A class that will be excluded
     * @return A new exclusion instance for the class
     */
    static ClassSecurityExclude ofClass(Class<?> value) {
        return new ClassSecurityExclude(value);
    }

    /**
     * Creates a new exclusion instance for the path.
     *
     * @param value A path that will be excluded
     * @return A new exclusion instance for the path
     */
    static PathSecurityExclude ofPath(String value) {
        return new PathSecurityExclude(value);
    }
}
