# Runtime Selection

Scriptify defines runtime-independent contracts in `api`. JavaScript execution is provided by runtime modules.

## GraalVM Runtime

Class:

```java
org.densy.scriptify.js.graalvm.script.JsScript
```

Return type:

```java
Script<Value>
```

GraalVM uses `org.graalvm.polyglot.Context` and evaluates JavaScript as modules.

Supported behavior:

- ES module source evaluation;
- imports through a virtual module file system;
- internal and external Scriptify modules;
- Java functions through `ProxyExecutable`;
- Java values/classes through GraalVM host interop;
- `ScriptAccess.ALL`;
- `ScriptAccess.EXPLICIT` through GraalVM `HostAccess.EXPLICIT`;
- class lookup restrictions through `allowHostClassLookup`;
- path restrictions for external file modules through Scriptify path access.

Use GraalVM when you need modern JavaScript module behavior and stronger engine-level host access controls.

## Rhino Runtime

Class:

```java
org.densy.scriptify.js.rhino.script.JsScript
```

Return type:

```java
Script<Object>
```

Rhino uses `org.mozilla.javascript.Context` in ES6 mode.

Supported behavior:

- one-shot and compiled JavaScript evaluation;
- internal Scriptify modules;
- external Scriptify modules through source transformation;
- supported top-level import/export forms;
- Java functions through Rhino `Function`;
- `ScriptAccess.ALL`;
- `ScriptAccess.EXPLICIT` through restricted Java object/class wrappers;
- class lookup restrictions through Rhino `ClassShutter`.

Rhino is useful when you want a lighter runtime dependency. Its module support is Scriptify-provided, not a full ECMAScript module implementation.

## Runtime Differences

| Feature | GraalVM | Rhino |
| --- | --- | --- |
| Evaluation result | `org.graalvm.polyglot.Value` | Java/Rhino object |
| Module execution | Native ESM-style source | Source transformation |
| Java function bridge | `ProxyExecutable` | Rhino `Function` |
| `ScriptAccess.EXPLICIT` | GraalVM host access | Restricted wrappers |
| External file modules | Virtual file system | Loaded and transformed by Scriptify |
| Best fit | Modern JS/module use | Lightweight embedding |

## Import Recommendation

Use the same Scriptify module API with both runtimes:

```java
script.getModuleManager().addModule(module);
```

Then import from JavaScript:

```js
import * as app from "app";
```
