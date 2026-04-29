# Constants

Constants expose named Java values to scripts.

## Constant Interface

```java
org.densy.scriptify.api.script.constant.ScriptConstant
```

Create a constant:

```java
ScriptConstant constant = ScriptConstant.of("appName", "Scriptify");
```

Or implement it:

```java
public final class AppNameConstant implements ScriptConstant {
    @Override
    public String getName() {
        return "appName";
    }

    @Override
    public Object getValue() {
        return "Scriptify";
    }
}
```

## Register Globally

```java
script.getConstantManager().register(ScriptConstant.of("appName", "Scriptify"));
script.evalOneShot("appName");
```

Global constants are exported to the global module during compilation.

## Export From a Module

```java
SimpleScriptInternalModule app = new SimpleScriptInternalModule("app");
app.export(new ScriptConstantExport(ScriptConstant.of("name", "Scriptify")));
script.getModuleManager().addModule(app);
```

```js
import { name } from "app";
name;
```

## Constant Manager

`ScriptConstantManager` provides:

```java
Map<String, ScriptConstant> getConstants();
ScriptConstant getConstant(String name);
void register(ScriptConstant constant);
void remove(String name);
```

Default implementation:

```java
org.densy.scriptify.core.script.constant.StandardConstantManager
```

## Built-In Core Constants

| Constant | Value |
| --- | --- |
| `baseDir` | Current JVM working directory as an absolute string. |
| `osName` | `System.getProperty("os.name")`. |

These are exported by `StandardScriptModule`.

## Deprecated Common Manager

`CommonConstantManager` is deprecated. Prefer module exports.
