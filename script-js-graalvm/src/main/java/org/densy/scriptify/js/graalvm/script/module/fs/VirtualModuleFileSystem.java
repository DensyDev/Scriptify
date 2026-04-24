package org.densy.scriptify.js.graalvm.script.module.fs;

import org.densy.scriptify.api.exception.ScriptModuleLoadException;
import org.densy.scriptify.api.script.module.*;
import org.densy.scriptify.api.script.module.export.resolver.ScriptModuleExportResolver;
import org.densy.scriptify.api.script.security.SecurityPathAccessor;
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
    private final Supplier<ScriptModuleExportResolver> resolverSupplier;
    private final SecurityPathAccessor securityAccessor;

    private final Map<String, Path> modulePathCache = new HashMap<>();

    public VirtualModuleFileSystem(
            ScriptModuleManager moduleManager,
            Supplier<Context> contextSupplier,
            Supplier<ScriptModuleExportResolver> resolverSupplier,
            SecurityPathAccessor securityAccessor
    ) {
        this.moduleManager = moduleManager;
        this.contextSupplier = contextSupplier;
        this.resolverSupplier = resolverSupplier;
        this.securityAccessor = securityAccessor;
    }

    @Override
    public Path parsePath(String path) {
        // internal java-module
        if (moduleManager.getModule(path) instanceof ScriptInternalModule) {
            return resolveVirtualPath(path);
        }

        // external module
        if (moduleManager.getModule(path) instanceof ScriptExternalModule external) {
            return resolveExternalPath(path, external);
        }

        // real file — resolve via security accessor (relative to basePath if needed)
        return securityAccessor.getAccessiblePath(path);
    }

    @Override
    public Path parsePath(URI uri) {
        if (SCHEME.equals(uri.getScheme())) {
            return resolveVirtualPath(uri.getHost());
        }

        Path path = real.parsePath(uri);
        if (!securityAccessor.isAccessible(path.toString())) {
            throw new IllegalArgumentException("Access denied by security policy: " + uri);
        }

        return path;
    }

    @Override
    public void checkAccess(Path path, Set<? extends AccessMode> modes, LinkOption... linkOptions) throws IOException {
        if (this.isVirtual(path)) {
            return;
        }

        // check the access to real path via security path accessor
        if (!securityAccessor.isAccessible(path.toString())) {
            throw new AccessDeniedException(path.toString(), null, "Access denied by security policy");
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

        // check the access to real path via security path accessor
        if (!securityAccessor.isAccessible(path.toString())) {
            throw new AccessDeniedException(path.toString(), null, "Access denied by security policy");
        }

        return real.readAttributes(path, attributes, options);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        if (this.isVirtual(path)) {
            String moduleName = getModuleName(path);

            if (moduleManager.getModule(moduleName) instanceof ScriptInternalModule internal) {
                byte[] source = JsModuleSourceGenerator
                        .generateModuleSource(
                                contextSupplier.get(),
                                internal,
                                resolverSupplier.get()
                        )
                        .getBytes(StandardCharsets.UTF_8);
                return new ByteArrayChannel(source);
            }

            if (moduleManager.getModule(moduleName) instanceof ScriptExternalModule external) {
                try {
                    return new ByteArrayChannel(external.load());
                } catch (ScriptModuleLoadException e) {
                    throw new IOException("Failed to load external module: " + moduleName, e);
                }
            }

            throw new IOException("Script module not found: " + moduleName);
        }
        return real.newByteChannel(path, options, attrs);
    }

    @Override
    public Path toAbsolutePath(Path path) {
        if (isVirtual(path)) {
            return path;
        }

        // resolve the path relative to basePath
        if (!path.isAbsolute()) {
            return securityAccessor.getBasePath().resolve(path).normalize();
        }

        return real.toAbsolutePath(path);
    }

    @Override
    public Path toRealPath(Path path, LinkOption... linkOptions) throws IOException {
        if (isVirtual(path)) {
            return path;
        }

        if (!securityAccessor.isAccessible(path.toString())) {
            throw new AccessDeniedException(path.toString(), null, "Access denied by security policy");
        }

        return real.toRealPath(path, linkOptions);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        if (!securityAccessor.isAccessible(dir.toString())) {
            throw new AccessDeniedException(dir.toString(), null, "Access denied by security policy");
        }
        return real.newDirectoryStream(dir, filter);
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        if (!securityAccessor.isAccessible(dir.toString())) {
            throw new AccessDeniedException(dir.toString(), null, "Access denied by security policy");
        }
        real.createDirectory(dir, attrs);
    }

    @Override
    public void delete(Path path) throws IOException {
        if (!securityAccessor.isAccessible(path.toString())) {
            throw new AccessDeniedException(path.toString(), null, "Access denied by security policy");
        }
        real.delete(path);
    }

    private Path resolveVirtualPath(String moduleName) {
        return modulePathCache.computeIfAbsent(moduleName, name -> Paths.get(
                System.getProperty("java.io.tmpdir"),
                "scriptify",
                JsModuleSourceGenerator.encodeModuleName(name) + ".mjs"
        ));
    }

    private Path resolveExternalPath(String moduleName, ScriptExternalModule external) {
        if (external instanceof ScriptFileExternalModule fileModule) {
            Path target = Files.isDirectory(fileModule.getPath())
                    ? fileModule.getPath().resolve(fileModule.getEntryPoint())
                    : fileModule.getPath();
            if (!securityAccessor.isAccessible(target.toString())) {
                throw new IllegalArgumentException("Access denied by security policy: " + target);
            }
            return target;
        }
        return resolveVirtualPath(moduleName);
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
