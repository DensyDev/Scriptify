package org.densy.scriptify.api.script.security;

import java.io.File;
import java.nio.file.Path;

/**
 * Provides secure access to files and paths, ensuring
 * all operations are restricted to the configured base path.
 */
public interface SecurityFileSystem {

    /**
     * Returns a secure Path inside the base path.
     *
     * @param path the path string to resolve
     * @return a normalized and secure Path
     * @throws SecurityException if the path is outside basePath or not accessible
     */
    Path getPath(String path);

    /**
     * Returns a secure File inside the base path.
     *
     * @param path the path string to resolve
     * @return a normalized and secure File
     * @throws SecurityException if the path is outside basePath or not accessible
     */
    File getFile(String path);

    /**
     * Returns the base path for this file system.
     */
    Path getBasePath();
}
