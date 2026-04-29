# Scriptify Documentation

Scriptify is a JVM library for embedding JavaScript execution into Java applications. It provides script runtimes, Java function exports, constants, modules, host-access control, and security-aware file access.

This documentation is organized by feature. Start with installation and runtime selection, then move to the API areas you need.

## Contents

| Page | Topic |
| --- | --- |
| [Installation](installation.md) | Gradle/Maven coordinates and module artifacts. |
| [Runtime Selection](runtimes.md) | GraalVM vs Rhino behavior and tradeoffs. |
| [Script Lifecycle](script-lifecycle.md) | `evalOneShot`, `compile`, `CompiledScript`, `addExtraScript`. |
| [Functions](functions.md) | Custom Java functions, annotations, argument binding, managers. |
| [Constants](constants.md) | Global constants and module constant exports. |
| [Modules](modules.md) | Internal modules, exports, global module, import support. |
| [External Modules](external-modules.md) | File and stream modules, GraalVM/Rhino differences. |
| [ScriptAccess](script-access.md) | `ALL`, `EXPLICIT`, and `@ScriptAccess.Export`. |
| [Security](security.md) | Class/path restrictions, security mode, safe exposure patterns. |
| [Standard Module](standard-module.md) | Utility, file, crypto, random, OS, and zip functions. |
| [HTTP Module](http-module.md) | `HttpScriptModule`, `HttpRequest`, methods, output types. |
| [Custom Runtime](custom-runtime.md) | Implementing another engine behind Scriptify APIs. |
| [Exceptions](exceptions.md) | Error model and exception types. |
| [Limitations](limitations.md) | Runtime differences, deprecated APIs, known constraints. |

## Minimal Setup

```java
import org.densy.scriptify.js.graalvm.script.JsScript;

JsScript script = new JsScript();
Object result = script.evalOneShot("1 + 2 + 3");
```

For Rhino, use `org.densy.scriptify.js.rhino.script.JsScript`.

## Recommended API Style

Use modules for new integrations:

```java
SimpleScriptInternalModule module = new SimpleScriptInternalModule("app");
module.export(new ScriptValueExport("version", "1.0.0"));
script.getModuleManager().addModule(module);
```

```js
import { version } from "app";
version;
```

Global function and constant managers still exist, but module exports are easier to reason about and are the preferred surface for application APIs.
