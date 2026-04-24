package org.densy.scriptify.api.script.module;

import java.nio.file.Path;

/**
 * An external module backed by a file or directory on disk.
 * <p>
 * If path points to a directory, entry point is resolved automatically.
 */
public interface ScriptFileExternalModule extends ScriptExternalModule {
    Path getPath();

    /**
     * Gets entry point filename when path is a directory.
     * */
    default String getEntryPoint() {
        return "index.mjs";
    }
}
