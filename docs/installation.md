# Installation

Scriptify is published as several artifacts. Choose the JavaScript runtime you want and add optional modules as needed.

## Repository

```kotlin
repositories {
    maven {
        name = "densyRepositorySnapshots"
        url = uri("https://repo.densy.org/snapshots")
    }
}
```

Maven:

```xml
<repositories>
    <repository>
        <id>densy-repository-snapshots</id>
        <url>https://repo.densy.org/snapshots</url>
    </repository>
</repositories>
```

## Runtime Artifacts

Use one runtime artifact in most applications:

```kotlin
dependencies {
    implementation("org.densy.scriptify:script-js-graalvm:1.6.1-SNAPSHOT")
}
```

or:

```kotlin
dependencies {
    implementation("org.densy.scriptify:script-js-rhino:1.6.1-SNAPSHOT")
}
```

## Optional Artifacts

| Artifact | Use |
| --- | --- |
| `api` | Interfaces and exceptions for integrations. |
| `core` | Default managers, modules, exports, and security implementations. |
| `common` | Standard utility module. |
| `http` | HTTP module built on OkHttp. |
| `script-js-graalvm` | GraalVM JavaScript runtime. |
| `script-js-rhino` | Rhino JavaScript runtime. |

Add optional modules directly when your application references their classes:

```kotlin
dependencies {
    implementation("org.densy.scriptify:common:1.6.1-SNAPSHOT")
    implementation("org.densy.scriptify:http:1.6.1-SNAPSHOT")
}
```

## Java Version

The project is built with a Java 17 toolchain.

## Choosing Dependencies

For a simple app that runs JavaScript and exposes your own modules, a runtime artifact is enough.

For an app that imports the standard utility module:

```kotlin
implementation("org.densy.scriptify:script-js-graalvm:1.6.1-SNAPSHOT")
implementation("org.densy.scriptify:common:1.6.1-SNAPSHOT")
```

For HTTP:

```kotlin
implementation("org.densy.scriptify:script-js-graalvm:1.6.1-SNAPSHOT")
implementation("org.densy.scriptify:http:1.6.1-SNAPSHOT")
```
