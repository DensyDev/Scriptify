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
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Represents a function to archive specific files to archive file based on regex or full file names.
 */
public class ScriptFunctionSmartZipFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "smartZipFile";
    }

    @ExecuteAt
    public void execute(
            @Executor Script<?> script,
            @Argument(name = "filesPath") String filesPath,
            @Argument(name = "compressedFilePath") String compressedFilePath,
            @Argument(name = "patterns") List<String> patterns
    ) {
        try {
            File filesToZip = script.getSecurityManager().getFileSystem().getFile(filesPath);
            File compressedFile = script.getSecurityManager().getFileSystem().getFile(compressedFilePath);
            FileOutputStream fos = new FileOutputStream(compressedFile);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            List<Pattern> regexPatterns = patterns.stream()
                    .map(Pattern::compile)
                    .toList();

            if (filesToZip.isDirectory()) {
                for (File file : Objects.requireNonNull(filesToZip.listFiles())) {
                    String fileName = file.getName();
                    boolean matches = regexPatterns.stream().anyMatch(pattern -> pattern.matcher(fileName).matches());

                    if (matches) {
                        zipFile(file, fileName, zipOut);
                    }
                }
            }

            zipOut.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zipOut.putNextEntry(new ZipEntry(fileName));
            zipOut.closeEntry();

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + childFile.getName(), zipOut);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(fileToZip)) {
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zipOut.write(buffer, 0, length);
                }
            }
        }
    }
}
