package org.densy.scriptify.api.script.security;

import java.nio.file.Path;

/**
 * Manages path access based on security constraints, ensuring only safe paths are accessible.
 */
public interface SecurityPathAccessor {

    /**
     * Gets a base path for this accessor, which will be used for relative path calculations.
     *
     * @return The base path to set
     */
    Path getBasePath();

    /**
     * Sets a new base path for this accessor, which will be used for relative path calculations.
     *
     * @param basePath The new base path to set
     */
    void setBasePath(Path basePath);

    /**
     * Returns a path that is safe to access according to security rules. If the path is not accessible,
     * it returns a path relative to the base path with ':' characters removed to prevent potential path traversal attacks.
     *
     * @param path The path string to be checked and possibly modified
     * @return A Path object representing the accessible path or a sanitized version if not accessible
     */
    Path getAccessiblePath(String path);

    /**
     * Checks if the given path is accessible based on the current security settings.
     *
     * @param path The path to check for access permission
     * @return true if the path is accessible, false otherwise
     */
    boolean isAccessible(String path);
}
