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
     * If the path is accessible via exclusions, returns the normalized path.
     * If the path is not accessible, creates a safe path within basePath by cleaning the path from invalid characters.
     *
     * @param path The path string to be checked and possibly modified
     * @return A Path object representing the accessible path or a path within base directory
     */
    @Override
    public Path getAccessiblePath(String path) {
        if (this.isAccessible(path)) {
            // Path is in exclusions - return it normalized
            return Paths.get(path).normalize().toAbsolutePath();
        }

        // Path is not accessible - create safe path within basePath
        // We need to manually combine paths because resolve() ignores basePath for absolute paths
        Path safePath = Paths.get(basePath.toString(), path.replace(":", "")).normalize();

        // CRITICAL: Ensure the result stays within basePath boundaries
        if (!safePath.startsWith(basePath)) {
            // If path tries to escape basePath (e.g., "../"), return basePath itself
            return basePath;
        }

        return safePath;
    }

    /**
     * Checks if the given path is accessible based on the current security settings.
     *
     * @param path The path to check for access permission
     * @return true if the path is accessible, false otherwise
     */
    @Override
    public boolean isAccessible(String path) {
        if (!securityManager.getSecurityMode()) {
            return true;
        }

        // Normalize the path to resolve .. and . components to prevent path traversal
        Path normalizedPath;
        try {
            normalizedPath = Paths.get(path).normalize().toAbsolutePath();
        } catch (Exception e) {
            return false;
        }

        // Check both original and normalized path against exclusions for compatibility
        String normalizedPathString = normalizedPath.toString();

        // Search all exclusions and check that the path is excluded
        for (SecurityExclude exclude : securityManager.getExcludes()) {
            if (exclude instanceof PathSecurityExclude) {
                // Check original path first
                if (exclude.isExcluded(path)) {
                    return true;
                }

                // Check normalized path
                if (exclude.isExcluded(normalizedPathString)) {
                    return true;
                }

                // Check with forward slashes for cross-platform compatibility
                String pathWithForwardSlashes = normalizedPathString.replace('\\', '/');
                if (exclude.isExcluded(pathWithForwardSlashes)) {
                    return true;
                }
            }
        }

        return false;
    }
}
