package org.densy.scriptify.core.script.security;

import org.densy.scriptify.api.script.security.SecurityFileSystem;
import org.densy.scriptify.api.script.security.SecurityPathAccessor;

import java.io.File;
import java.nio.file.Path;

public class SecurityFileSystemImpl implements SecurityFileSystem {

    private final SecurityPathAccessor pathAccessor;

    public SecurityFileSystemImpl(SecurityPathAccessor pathAccessor) {
        this.pathAccessor = pathAccessor;
    }

    @Override
    public Path getPath(String path) {
        return pathAccessor.getAccessiblePath(path);
    }

    @Override
    public File getFile(String path) {
        return this.getPath(path).toFile();
    }

    @Override
    public Path getBasePath() {
        return pathAccessor.getBasePath();
    }
}
