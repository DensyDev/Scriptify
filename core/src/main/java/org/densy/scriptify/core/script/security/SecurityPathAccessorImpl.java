package org.densy.scriptify.core.script.security;

import org.densy.scriptify.api.script.security.ScriptSecurityManager;
import org.densy.scriptify.api.script.security.SecurityPathAccessor;
import org.densy.scriptify.api.script.security.exclude.PathSecurityExclude;
import org.densy.scriptify.api.script.security.exclude.SecurityExclude;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages path access based on security constraints, ensuring only safe paths are accessible.
 */
public class SecurityPathAccessorImpl implements SecurityPathAccessor {

    private final ScriptSecurityManager securityManager;
    private Path basePath;

    /**
     * Constructs a SecurityPathAccessor with the default base path set to the current working directory.
     *
     * @param securityManager The security manager to check access permissions
     */
    public SecurityPathAccessorImpl(ScriptSecurityManager securityManager) {
        this(securityManager, Paths.get("").toAbsolutePath());
    }

    /**
     * Constructs a SecurityPathAccessor with a specified base path for relative path calculations.
     *
     * @param securityManager The security manager to check access permissions
     * @param basePath The base path from which relative paths are calculated
     */
    public SecurityPathAccessorImpl(ScriptSecurityManager securityManager, Path basePath) {
        this.securityManager = securityManager;
        this.basePath = basePath;
    }

    /**
     * Gets a base path for this accessor, which will be used for relative path calculations.
     *
     * @return The base path to set
     */
    @Override
    public Path getBasePath() {
        return basePath;
    }

    /**
     * Sets a new base path for this accessor, which will be used for relative path calculations.
     *
     * @param basePath The new base path to set
     */
    @Override
    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }

    /**
     * Returns a path that is safe to access according to security rules.
     *
     * @param path The path string to be checked and possibly modified
     * @return a Path object representing the accessible path or a path within base directory
     * @throws SecurityException if path cannot be accessed
     */
    @Override
    public Path getAccessiblePath(String path) {
        if (this.isAccessible(path)) {
            return basePath.resolve(path).normalize().toAbsolutePath();
        }
        throw new SecurityException("Access denied by security policy: " + path);
    }

    /**
     * Checks if the given path is accessible based on the current security settings.
     *
     * @param path the path to check for access permission
     * @return true if the path is accessible, otherwise false
     */
    @Override
    public boolean isAccessible(String path) {
        if (!securityManager.getSecurityMode()) {
            return true;
        }

        Path normalizedPath;
        try {
            normalizedPath = basePath.resolve(path).normalize().toAbsolutePath();
        } catch (Exception e) {
            return false;
        }

        String normalizedStr = normalizedPath.toString().replace('\\', '/');

        for (SecurityExclude exclude : securityManager.getExcludes()) {
            if (exclude.isExcluded(path) || exclude.isExcluded(normalizedStr)) {
                return true;
            }
        }

        return false;
    }
}
