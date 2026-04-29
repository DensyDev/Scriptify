ммм# Scriptify
Scriptify is a JVM library for embedding JavaScript execution into Java applications. It provides script runtimes, Java function exports, constants, modules, host-access control, and security-aware file access.

## What is it for?
This library is designed to execute JavaScript scripts and has the ability to register global functions and constants.
It also allows you to configure security for executing scripts.

## Quick Start

```java
import org.densy.scriptify.api.exception.ScriptException;
import org.densy.scriptify.js.graalvm.script.JsScript;

public class Main {
    public static void main(String[] args) throws ScriptException {
        JsScript script = new JsScript();
        Object result = script.evalOneShot("1 + 2 + 3");
        System.out.println(result);
    }
}
```

For Rhino use `org.densy.scriptify.js.rhino.script.JsScript` instead.

## Documentation

Full documentation is available in [docs/index.md](docs/index.md).

## Other scripts support
- [TypeScript](https://github.com/DensyDev/Scriptify-TypeScript) - TS support using swc4j
- [TypeScript Declaration Generator](https://github.com/DensyDev/Scriptify-DTS-Generator) - Declaration generator for JS or TS
- [Kotlin Script](https://github.com/DensyDev/Scriptify-Kotlin-Script) - Kotlin Script support

## Maven
Adding repository:
```xml
<repositories>
    <repository>
        <id>densy-repository-snapshots</id>
        <url>https://repo.densy.org/snapshots</url>
    </repository>
</repositories>
```

For adding a library only:
```xml
<dependency>
    <groupId>org.densy.scriptify</groupId>
    <artifactId>core</artifactId>
    <version>1.6.0-SNAPSHOT</version>
</dependency>
```

For adding a library with JS for Rhino or GraalVM:
```xml
<dependency>
    <groupId>org.densy.scriptify</groupId>
    <artifactId>script-js-rhino</artifactId>
    <version>1.6.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.densy.scriptify</groupId>
    <artifactId>script-js-graalvm</artifactId>
    <version>1.6.0-SNAPSHOT</version>
</dependency>
```
## Gradle
Adding repository:
```groovy
maven {
    name "densyRepositorySnapshots"
    url "https://repo.densy.org/snapshots"
}
```

For adding a library only:
```groovy
implementation "org.densy.scriptify:core:1.6.0-SNAPSHOT"
```

For adding a library with JS for Rhino or GraalVM:
```groovy
implementation "org.densy.scriptify:script-js-rhino:1.6.0-SNAPSHOT"
implementation "org.densy.scriptify:script-js-graalvm:1.6.0-SNAPSHOT"
```
