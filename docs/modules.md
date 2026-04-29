# Modules

Modules are the preferred way to expose application APIs to scripts.

## Module Manager

```java
ScriptModuleExportResolverFactory getModuleExportResolver();
void setModuleExportResolver(ScriptModuleExportResolverFactory factory);

ScriptAccess getScriptAccess();
void setScriptAccess(ScriptAccess scriptAccess);

ScriptInternalModule getGlobalModule();
Map<String, ScriptModule> getModules();
ScriptModule getModule(String name);
void addModule(ScriptModule module);
void removeModule(String name);
```

Every runtime has its own module manager implementation.

## Module Types

| Type | Interface | Description |
| --- | --- | --- |
| Internal | `ScriptInternalModule` | Java-defined exports. |
| External | `ScriptExternalModule` | JavaScript source loaded from file or bytes. |

## Internal Module

```java
SimpleScriptInternalModule app = new SimpleScriptInternalModule("app");
app.export(new ScriptValueExport("version", "1.0.0"));
script.getModuleManager().addModule(app);
```

```js
import { version } from "app";
version;
```

## Global Module

Exports in `getGlobalModule()` are available without import.

Runtime implementations also copy globally registered functions and constants into the global module before compilation.

Use global exports sparingly. Modules are clearer for larger APIs.

## Export Types

| Export | Description |
| --- | --- |
| `ScriptFunctionExport` | Exports a `ScriptFunction`; runtime creates a callable wrapper. |
| `ScriptFunctionDefinitionExport` | Exports an existing function definition. |
| `ScriptConstantExport` | Exports a `ScriptConstant`. |
| `ScriptValueExport` | Exports a Java value, object instance, enum, or `Class<?>`. |

## Java Value Exports

```java
module.export(new ScriptValueExport("config", Map.of("mode", "dev")));
module.export(new ScriptValueExport("Service", UserService.class));
module.export(new ScriptValueExport("service", new UserService()));
```

Access to Java members depends on `ScriptAccess`.

## Copying Modules

`ScriptInternalModule.copy` copies exports from another internal module.

```java
SimpleScriptInternalModule std = new SimpleScriptInternalModule("std");
std.copy(new StandardScriptModule());
script.getModuleManager().addModule(std);
```

Copy rejects conflicting exports with the same name and different hash.

## Import Syntax

GraalVM supports native module imports through its Scriptify virtual file system.

Rhino supports these top-level forms:

```js
import * as name from "module";
import { value, other as alias } from "module";
import defaultValue from "module";
import "module";
```

For runtime-specific details, see [Runtime Selection](runtimes.md) and [External Modules](external-modules.md).
