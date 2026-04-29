# Custom Runtime

Scriptify can support additional scripting runtimes by implementing `Script<T>`.

## Required Contract

```java
public final class MyScript implements Script<Object> {
    private final ScriptSecurityManager securityManager = new StandardSecurityManager();
    private final ScriptFunctionManager functionManager = new StandardFunctionManager();
    private final ScriptConstantManager constantManager = new StandardConstantManager();
    private final ScriptModuleManager moduleManager = createModuleManager();

    @Override
    public ScriptSecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public ScriptModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public ScriptFunctionManager getFunctionManager() {
        return functionManager;
    }

    @Override
    public ScriptConstantManager getConstantManager() {
        return constantManager;
    }

    @Override
    public CompiledScript<Object> compile(String source) throws ScriptException {
        throw new UnsupportedOperationException();
    }
}
```

## Runtime Responsibilities

A runtime implementation should:

- create the engine context;
- configure security before script execution;
- expose global functions;
- expose global constants;
- expose internal modules;
- load external modules;
- apply `ScriptAccess.ALL` and `ScriptAccess.EXPLICIT`;
- convert script values into Java values before invoking `ScriptFunction`;
- convert Java return values back into runtime values;
- wrap runtime failures in `ScriptException`;
- close engine resources in `CompiledScript.close`.

## Function Bridge

Your runtime needs an engine-specific callable wrapper for `ScriptFunctionDefinition`.

That wrapper should:

- receive script arguments;
- convert them to Java values;
- find a matching `ScriptFunctionExecutor`;
- call `executor.execute(script, args...)`;
- convert the result back to the script engine.

## Module Export Resolver

Implement:

```java
ScriptModuleExportResolverFactory
ScriptModuleExportResolver
```

The resolver maps Scriptify exports to runtime values:

- `ScriptFunctionExport`;
- `ScriptFunctionDefinitionExport`;
- `ScriptConstantExport`;
- `ScriptValueExport`;
- custom `ScriptExport` implementations if you add them.

Throw `ScriptModuleWrongContextException` if the factory receives a context object from another runtime.

## Security Integration

If the engine supports host class lookup restrictions, wire it to `ScriptSecurityManager.getExcludes()`.

If the engine supports file system hooks, route file paths through:

```java
script.getSecurityManager().getPathAccessor()
```

If the engine exposes Java objects/classes, implement `ScriptAccess.EXPLICIT`.
