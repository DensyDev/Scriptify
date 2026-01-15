package org.densy.scriptify.common.script.function.impl.file;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * Represents a function to download file from url
 */
public class ScriptFunctionDownloadFromUrl implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "downloadFromUrl";
    }

    @ExecuteAt
    public void execute(
            @Executor Script<?> script,
            @Argument(name = "url") String url,
            @Argument(name = "filePath") String filePath
    ) {
        try (InputStream in = new URI(url).toURL().openStream()) {
            File targetPath = script.getSecurityManager().getFileSystem().getFile(filePath);
            Files.copy(in, targetPath.toPath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
