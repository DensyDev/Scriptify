# Exceptions

Scriptify wraps script and integration failures in typed exceptions.

## Base Exception

```java
ScriptException
```

Base checked exception for script failures.

## Function Exceptions

| Exception | Meaning |
| --- | --- |
| `ScriptFunctionException` | Function invocation failed. |
| `ScriptFunctionArgumentCountMismatchException` | Script called a function with too few or too many arguments. |
| `ScriptFunctionArgumentTypeMismatchException` | A script argument could not be assigned to the Java executor parameter type. |

Function executor failures are wrapped in `ScriptFunctionException`.

## Module Exceptions

| Exception | Meaning |
| --- | --- |
| `ScriptModuleLoadException` | External module source could not be loaded. |
| `ScriptModuleCopyException` | Copying module exports encountered a conflicting export. |
| `ScriptModuleWrongContextException` | A module export resolver received an incompatible runtime context. |

## Runtime Wrapping

Runtime implementations catch engine-specific errors and usually wrap them in `ScriptException`.

Example:

```java
try {
    script.evalOneShot(source);
} catch (ScriptException e) {
    throw new RuntimeException("Script failed", e);
}
```

## Security Exceptions

Path access may throw `SecurityException` when `securityMode` is enabled and a path is not allowed.

Security exceptions can be wrapped by runtime code if they occur during script evaluation.
