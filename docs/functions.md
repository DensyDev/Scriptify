# Functions

Functions expose Java methods to scripts.

## Function Interface

A function implements:

```java
org.densy.scriptify.api.script.function.ScriptFunction
```

Minimal function:

```java
public final class AddFunction implements ScriptFunction {
    @Override
    public String getName() {
        return "add";
    }

    @ExecuteAt
    public Number execute(
            @Argument(name = "left") Number left,
            @Argument(name = "right") Number right
    ) {
        return left.doubleValue() + right.doubleValue();
    }
}
```

## Register Globally

```java
script.getFunctionManager().register(new AddFunction());
script.evalOneShot("add(2, 3)");
```

Global functions are added to the runtime global module during compilation.

## Export From a Module

```java
SimpleScriptInternalModule math = new SimpleScriptInternalModule("math");
math.export(new ScriptFunctionExport(new AddFunction()));
script.getModuleManager().addModule(math);
```

```js
import * as math from "math";
math.add(2, 3);
```

## Execution Methods

Mark callable methods with `@ExecuteAt`.

```java
@ExecuteAt
public String execute(@Argument(name = "value") String value) {
    return value.toUpperCase();
}
```

A single function class may have multiple `@ExecuteAt` methods. Runtime dispatch selects a compatible executor by argument count and vararg compatibility; the core executor then validates Java types.

Avoid ambiguous overloads with the same script argument count unless conversion is predictable.

## Arguments

Use `@Argument` for script-provided values.

```java
@Argument(name = "filePath") String filePath
```

Optional arguments:

```java
@Argument(name = "recursive", required = false) Boolean recursive
```

Varargs:

```java
@ExecuteAt
public void execute(@Argument(name = "args") Object... args) {
}
```

## Executor Injection

Use `@Executor` to receive the current `Script<?>`.

```java
@ExecuteAt
public String execute(
        @Executor Script<?> script,
        @Argument(name = "filePath") String filePath
) {
    Path path = script.getSecurityManager().getFileSystem().getPath(filePath);
    return Files.readString(path);
}
```

This is how standard file functions access security-aware paths.

## Function Manager

`ScriptFunctionManager` provides:

```java
ScriptFunctionDefinitionFactory getFunctionDefinitionFactory();
void setFunctionDefinitionFactory(ScriptFunctionDefinitionFactory factory);
Map<String, ScriptFunctionDefinition> getFunctions();
ScriptFunctionDefinition getFunction(String name);
void register(ScriptFunction function);
void remove(String name);
```

Default implementation:

```java
org.densy.scriptify.core.script.function.StandardFunctionManager
```

Behavior:

- duplicate function names are rejected;
- removing a missing function throws;
- returned maps are unmodifiable views;
- `ScriptFunctionDefinitionFactory` can be replaced for custom function metadata.

## Deprecated Common Manager

`CommonFunctionManager` exists for compatibility but is deprecated. Use `StandardScriptModule` or your own internal modules instead.
