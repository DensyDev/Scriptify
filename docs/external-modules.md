# External Modules

External modules load JavaScript source from files or byte streams.

## File Module

```java
script.getModuleManager().addModule(
    new SimpleScriptFileExternalModule("external", Path.of("external.mjs"))
);
```

If the path is a directory, `SimpleScriptFileExternalModule` loads `index.mjs` by default.

Custom entry point:

```java
new SimpleScriptFileExternalModule("external", Path.of("scripts"), "main.mjs");
```

## Stream Module

```java
script.getModuleManager().addModule(
    new SimpleScriptStreamExternalModule(
        "dynamic",
        "dynamic.mjs",
        "export const value = 10;".getBytes(StandardCharsets.UTF_8)
    )
);
```

Supplier-based modules reload bytes on every load:

```java
new SimpleScriptStreamExternalModule("dynamic", "dynamic.mjs", () -> loadBytes());
```

## GraalVM Behavior

GraalVM uses `VirtualModuleFileSystem`.

Behavior:

- internal modules are generated as virtual JavaScript module source;
- file external modules are resolved to real files after path security checks;
- stream external modules are loaded into virtual channels;
- scripts are evaluated as JavaScript modules.

Supported JavaScript:

```js
import { value } from "external";
```

## Rhino Behavior

Rhino loads source bytes and transforms supported imports/exports.

Supported import forms:

```js
import * as name from "module";
import { value, other as alias } from "module";
import defaultValue from "module";
import "module";
```

Supported export forms:

```js
export { value, internalName as publicName };
export const value = 1;
export let value = 1;
export var value = 1;
export function name() { }
export class Name { }
export default expression;
```

Rhino transformation is not a full JavaScript parser. Keep import/export declarations top-level and normally formatted.

## Security

File modules should be used with `securityMode` enabled when scripts or module paths are not fully trusted.

```java
script.getSecurityManager().setSecurityMode(true);
script.getSecurityManager().addExclude(SecurityExclude.ofPath("scripts"));
```
