package org.densy.scriptify.common.script.function.impl.zip;

import org.densy.scriptify.api.script.Script;
import org.densy.scriptify.api.script.function.ScriptFunction;
import org.densy.scriptify.api.script.function.annotation.Argument;
import org.densy.scriptify.api.script.function.annotation.ExecuteAt;
import org.densy.scriptify.api.script.function.annotation.Executor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Represents a function to unzip specific files from an archive based on regex or full file names.
 */
public class ScriptFunctionSmartUnzipFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "smartUnzipFile";
    }

    @ExecuteAt
    public void execute(
            @Executor Script<?> script,
            @Argument(name = "compressedFilePath") String compressedFilePath,
            @Argument(name = "decompressedPath") String decompressedPath,
            @Argument(name = "patterns") List<String> patterns
    ) {
        try {
            File fileCompressed = script.getSecurityManager().getFileSystem().getFile(compressedFilePath);
            File fileDecompressed = script.getSecurityManager().getFileSystem().getFile(decompressedPath);

            // Convert patterns to regex patterns
            List<Pattern> regexPatterns = patterns.stream()
                    .map(Pattern::compile)
                    .toList();

            byte[] buffer = new byte[1024];
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileCompressed))) {
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    String fileName = zipEntry.getName();

                    // Check if the file matches any pattern
                    boolean matches = regexPatterns.stream().anyMatch(pattern -> pattern.matcher(fileName).matches());
                    if (matches) {
                        File newFile = newFile(fileDecompressed, zipEntry);
                        if (zipEntry.isDirectory()) {
                            if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                throw new IOException("Failed to create directory " + newFile);
                            }
                        } else {
                            File parent = newFile.getParentFile();
                            if (!parent.isDirectory() && !parent.mkdirs()) {
                                throw new IOException("Failed to create directory " + parent);
                            }

                            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                            }
                        }
                    }

                    zipEntry = zis.getNextEntry();
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
