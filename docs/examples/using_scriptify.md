# Using Scriptify

___

### Using JS with GraalVM
```kotlin
implementation "org.densy.scriptify:script-js-graalvm:1.3.0-SNAPSHOT"
```
### Using JS with Rhino
```kotlin
implementation "org.densy.scriptify:script-js-rhino:1.3.0-SNAPSHOT"
```
___

Running the script (GraalVM):
```java
import org.densy.scriptify.js.graalvm.script.JsScript;
import org.densy.scriptify.core.script.StandardConstantManager;
import org.densy.scriptify.core.script.StandardFunctionManager;
import org.densy.scriptify.api.ScriptException;

JsScript script = new JsScript();
script.setFunctionManager(new StandardFunctionManager());
script.setConstantManager(new StandardConstantManager());
try {
    script.eval("print('Hello world from JS!')");
} catch(ScriptException e) {
    throw new RuntimeException(e);
}
```

Running a script from a file:
```java
script.eval(Files.readString(Path.of("./script.js")));
```