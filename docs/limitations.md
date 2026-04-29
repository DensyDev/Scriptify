# Limitations and Compatibility Notes

This page lists behavior that is important when designing a Scriptify integration.

## Deprecated APIs

Use:

```java
evalOneShot(source)
compile(source)
```

Avoid:

```java
eval(source)
```

`eval` is deprecated and delegates to `evalOneShot`.

`CommonFunctionManager` and `CommonConstantManager` are deprecated. Use modules instead.

## Runtime Differences

GraalVM and Rhino do not return identical value types.

- GraalVM returns `org.graalvm.polyglot.Value`.
- Rhino returns Java/Rhino objects.

If your application needs runtime-independent result handling, normalize results at your integration boundary.

## Rhino Module Parsing

Rhino module support is implemented by Scriptify source transformation.

Supported import/export forms are documented in [External Modules](external-modules.md). Avoid unusual formatting, nested import/export text, and import/export statements inside strings or comments.

## Security Scope

`ScriptAccess.EXPLICIT` controls members of exported Java classes and objects. It does not remove Scriptify functions that you explicitly export.

`securityMode` controls host class lookup and Scriptify path access. It does not automatically make arbitrary exported Java APIs safe.

## File Access

Path security applies when code uses `SecurityFileSystem` or runtime file hooks wired to `SecurityPathAccessor`.

If you export a Java object that performs file I/O internally, Scriptify cannot automatically enforce path restrictions inside that object.

## Standard Module Capabilities

`StandardScriptModule` is broad. It includes file access, command execution, environment access, zip operations, and network download.

For untrusted scripts, build a smaller internal module with only the functions you intend to expose.

## Function Overloads

Function executor matching is intentionally simple. Prefer clear overloads and avoid multiple `@ExecuteAt` methods with the same script argument count unless their Java types are easy to distinguish after runtime conversion.

## Mutable Managers

Managers are mutable. Configure script instances before compiling.

Compiled scripts should be treated as snapshots of the runtime configuration at compile time.
