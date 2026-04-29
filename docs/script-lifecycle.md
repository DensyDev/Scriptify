# Script Lifecycle

The central interface is:

```java
org.densy.scriptify.api.script.Script<T>
```

Runtime implementations provide concrete `JsScript` classes.

## Main Methods

```java
ScriptSecurityManager getSecurityManager();
ScriptModuleManager getModuleManager();
ScriptFunctionManager getFunctionManager();
ScriptConstantManager getConstantManager();

void addExtraScript(String script);
CompiledScript<T> compile(String script) throws ScriptException;
T evalOneShot(String script) throws ScriptException;
```

`eval(String)` still exists as a deprecated default method. Use `evalOneShot` or `compile`.

## One-Shot Evaluation

Use `evalOneShot` when the script is executed once.

```java
JsScript script = new JsScript();
Object result = script.evalOneShot("1 + 2");
```

The runtime compiles/evaluates the script and closes runtime resources.

## Compiled Scripts

Use `compile` when you need to keep a compiled script object.

```java
try (CompiledScript<?> compiled = script.compile("""
    (left, right) => left + right
    """)) {
    Object result = compiled.eval(2, 3);
}
```

Always close compiled scripts. They own runtime resources such as engine contexts.

## CompiledScript Contract

```java
T get();
T eval(Object... args);
void close();
```

Runtime behavior:

- GraalVM: `get()` returns the evaluated `Value`; `eval(args...)` executes it when it is callable.
- Rhino: `get()` executes the compiled script; `eval(args...)` executes it and calls the result if the result is a function.

## Extra Script

`addExtraScript` prepends code to every future compilation.

```java
script.addExtraScript("const prefix = 'app';");
script.evalOneShot("prefix + '-script'");
```

Extra scripts are applied in insertion order and become part of the final source.

## Configuration Timing

Configure these before compiling:

- security mode and excludes;
- module registrations;
- `ScriptAccess`;
- global functions;
- global constants;
- extra scripts.

Compiled scripts do not automatically inherit later manager changes unless the runtime recompiles a new script.
