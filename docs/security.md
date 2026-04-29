# Security

Scriptify security is opt-in. By default, `securityMode` is disabled.

```java
script.getSecurityManager().setSecurityMode(true);
```

When security mode is enabled, class and path access must be explicitly allowed through excludes.

## Security Manager API

```java
boolean getSecurityMode();
void setSecurityMode(boolean securityMode);

SecurityFileSystem getFileSystem();
SecurityPathAccessor getPathAccessor();

Set<SecurityExclude> getExcludes();
void addExclude(SecurityExclude exclude);
void removeExclude(SecurityExclude exclude);
```

Default implementation:

```java
org.densy.scriptify.core.script.security.StandardSecurityManager
```

## Class Access

Allow a class:

```java
script.getSecurityManager().addExclude(SecurityExclude.ofClass(java.time.Instant.class));
```

Allow a package prefix:

```java
script.getSecurityManager().addExclude(SecurityExclude.ofPackage("java.time"));
```

Runtime behavior:

- GraalVM uses excludes for host class lookup.
- Rhino uses excludes through `ClassShutter`.

## Path Access

Set a base path:

```java
script.getSecurityManager().getPathAccessor().setBasePath(Path.of("workspace"));
```

Allow a path prefix:

```java
script.getSecurityManager().addExclude(SecurityExclude.ofPath("workspace/input"));
```

Security path access resolves paths against the configured base path and normalizes them.

When access is denied, `SecurityFileSystem.getPath` throws `SecurityException`.

## File-System Surfaces

Path security is used by Scriptify-provided file operations:

- `existsFile`;
- `readFile`;
- `writeFile`;
- `deleteFile`;
- `moveFile`;
- `listFiles`;
- `downloadFromUrl`;
- zip/unzip functions;
- GraalVM external file module resolution.

## Powerful Capabilities

The standard and HTTP modules expose privileged operations:

| Capability | Risk |
| --- | --- |
| `execCommand` | Runs OS commands. |
| `env` | Reads environment variables. |
| file functions | Read/write/delete/move host files. |
| zip functions | Write extracted archive entries. |
| `downloadFromUrl` | Reads remote content and writes files. |
| HTTP module | Sends network requests. |

Do not expose these to untrusted scripts unless you have reviewed the capabilities and configured security.

## Recommended Safe Pattern

Create a narrow module instead of exporting `StandardScriptModule`.

```java
SimpleScriptInternalModule safe = new SimpleScriptInternalModule("safe");
safe.export(new ScriptFunctionExport(new ScriptFunctionPrint()));

script.getSecurityManager().setSecurityMode(true);
script.getModuleManager().setScriptAccess(ScriptAccess.EXPLICIT);
script.getModuleManager().addModule(safe);
```

Security mode restricts class/path access. Module design controls which Scriptify functions exist at all.

## Exclude Matching

`SecurityExclude.isExcluded` uses prefix matching:

```java
return value.startsWith(this.getValue());
```

Use precise prefixes and normalize paths consistently.
