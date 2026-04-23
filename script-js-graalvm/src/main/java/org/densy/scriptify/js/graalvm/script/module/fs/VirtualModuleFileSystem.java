package org.densy.scriptify.js.graalvm.script.module.fs;

import org.densy.scriptify.api.script.module.ScriptModule;
import org.densy.scriptify.api.script.module.ScriptModuleManager;
import org.densy.scriptify.js.graalvm.script.module.fs.util.ByteArrayChannel;
import org.densy.scriptify.js.graalvm.script.module.fs.util.JsModuleSourceGenerator;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.io.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class VirtualModuleFileSystem implements FileSystem {

    private static final String SCHEME = "scriptify";

    private final FileSystem real = FileSystem.newDefaultFileSystem();
    private final ScriptModuleManager moduleManager;
    private final Supplier<Context> contextSupplier;

    private final Map<String, Path> modulePathCache = new HashMap<>();

    public VirtualModuleFileSystem(ScriptModuleManager moduleManager, Supplier<Context> contextSupplier) {
        this.moduleManager = moduleManager;
        this.contextSupplier = contextSupplier;
    }

    @Override
    public Path parsePath(String path) {
        if (moduleManager.getModule(path) != null) {
            return this.resolveVirtualPath(path);
        }
        return real.parsePath(path);
    }

    @Override
    public Path parsePath(URI uri) {
        if (SCHEME.equals(uri.getScheme())) {
            return this.resolveVirtualPath(uri.getHost());
        }
        return real.parsePath(uri);
    }

    @Override
    public void checkAccess(Path path, Set<? extends AccessMode> modes, LinkOption... linkOptions) throws IOException {
        if (this.isVirtual(path)) {
            return;
        }
        real.checkAccess(path, modes, linkOptions);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        if (this.isVirtual(path)) {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("isRegularFile", true);
            attrs.put("isDirectory", false);
            attrs.put("size", 0L);
            attrs.put("lastModifiedTime", FileTime.fromMillis(0));
            attrs.put("creationTime", FileTime.fromMillis(0));
            attrs.put("lastAccessTime", FileTime.fromMillis(0));
            return attrs;
        }
        return real.readAttributes(path, attributes, options);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        if (this.isVirtual(path)) {
            String moduleName = getModuleName(path);
            ScriptModule module = moduleManager.getModule(moduleName);
            if (module == null) {
                throw new IOException("Scriptify module not found: " + moduleName);
            }
            byte[] source = JsModuleSourceGenerator
                    .generateModuleSource(contextSupplier.get(), module)
                    .getBytes(StandardCharsets.UTF_8);
            return new ByteArrayChannel(source);
        }
        return real.newByteChannel(path, options, attrs);
    }

    @Override
    public Path toAbsolutePath(Path path) {
        if (isVirtual(path)) return path;
        return real.toAbsolutePath(path);
    }

    @Override
    public Path toRealPath(Path path, LinkOption... linkOptions) throws IOException {
        if (isVirtual(path)) return path;
        return real.toRealPath(path, linkOptions);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return real.newDirectoryStream(dir, filter);
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        real.createDirectory(dir, attrs);
    }

    @Override
    public void delete(Path path) throws IOException {
        real.delete(path);
    }

    private Path resolveVirtualPath(String moduleName) {
        return modulePathCache.computeIfAbsent(moduleName, name -> Paths.get(
                System.getProperty("java.io.tmpdir"),
                "scriptify",
                JsModuleSourceGenerator.encodeModuleName(name) + ".mjs"
        ));
    }

    private boolean isVirtual(Path path) {
        return modulePathCache.containsValue(path);
    }

    private String getModuleName(Path path) {
        return modulePathCache.entrySet().stream()
                .filter(e -> e.getValue().equals(path))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown virtual path: " + path));
    }
}
