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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Represents a function to archive file to zip
 */
public class ScriptFunctionZipFile implements ScriptFunction {

    @Override
    public @NotNull String getName() {
        return "zipFile";
    }

    @ExecuteAt
    public void execute(
            @Executor Script<?> script,
            @Argument(name = "filePath") String filePath,
            @Argument(name = "compressedFilePath") String compressedFilePath
    ) {
        try {
            File fileToZip = script.getSecurityManager().getFileSystem().getFile(filePath);
            File compressedFile = script.getSecurityManager().getFileSystem().getFile(compressedFilePath);

            FileOutputStream fos = new FileOutputStream(compressedFile);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            zipFile(fileToZip, fileToZip.getName(), zipOut);
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
