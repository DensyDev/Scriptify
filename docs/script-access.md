# ScriptAccess

`ScriptAccess` controls which Java members scripts can access on exported Java classes and objects.

```java
script.getModuleManager().setScriptAccess(ScriptAccess.ALL);
script.getModuleManager().setScriptAccess(ScriptAccess.EXPLICIT);
```

## ALL

`ALL` is the default mode.

In this mode, scripts can access public Java members that the runtime exposes and the security manager allows.

Use `ALL` only when exported values/classes are intentionally part of the script API.

## EXPLICIT

`EXPLICIT` exposes only members annotated with:

```java
@ScriptAccess.Export
```

Supported elements:

- constructors;
- public instance methods;
- public static methods;
- public instance fields;
- public static fields;
- enum constants.

## Example

```java
public final class UserService {
    private final String user;

    @ScriptAccess.Export
    public UserService(String user) {
        this.user = user;
    }

    @ScriptAccess.Export
    public String getUser() {
        return user;
    }

    public String internalToken() {
        return "hidden";
    }
}
```

Export:

```java
SimpleScriptInternalModule services = new SimpleScriptInternalModule("services");
services.export(new ScriptValueExport("UserService", UserService.class));

script.getModuleManager().setScriptAccess(ScriptAccess.EXPLICIT);
script.getModuleManager().addModule(services);
```

JavaScript:

```js
import { UserService } from "services";

const service = new UserService("alice");
service.getUser();      // allowed
service.internalToken;  // unavailable
```

## GraalVM Implementation

GraalVM maps:

- `ScriptAccess.ALL` to `HostAccess.ALL`;
- `ScriptAccess.EXPLICIT` to `HostAccess.EXPLICIT`.

It also allows members annotated by `ScriptAccess.Export`.

## Rhino Implementation

Rhino uses restricted wrappers for Java classes and Java objects when `EXPLICIT` is enabled.

The wrapper exposes only annotated fields, methods, and constructors. Non-exported members are hidden from scripts.

## Scope of ScriptAccess

`ScriptAccess` affects Java members on values/classes exported through modules. It does not remove functions you explicitly export as `ScriptFunctionExport`.

For example, if you export `execCommand` as a Scriptify function, `ScriptAccess.EXPLICIT` does not block that function. Use smaller modules and security configuration for capability control.
