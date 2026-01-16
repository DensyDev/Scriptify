# Custom script runtime

You can register your script runtime as you do with Rhino or GraalVM

___

### Registration of custom runtime on the example of GraalVM

Let's create our script:
```java
import script.org.densy.scriptify.api.Script;
import constant.script.org.densy.scriptify.api.ScriptConstant;
import constant.script.org.densy.scriptify.api.ScriptConstantManager;
import function.script.org.densy.scriptify.api.ScriptFunction;
import function.script.org.densy.scriptify.api.ScriptFunctionManager;
import org.graalvm.polyglot.*;

public class JsScript implements Script {

    /**
     * Creating the runtime engine itself
     */
    private final Context context = Context.create();
    
    private ScriptFunctionManager functionManager;
    private ScriptConstantManager constantManager;

    // Getters and setters for function and constant managers
    
    @Override
    public ScriptFunctionManager getFunctionManager() {
        return functionManager;
    }

    @Override
    public void setFunctionManager(ScriptFunctionManager functionManager) {
        this.functionManager = functionManager;
    }

    @Override
    public ScriptConstantManager getConstantManager() {
        return constantManager;
    }

    @Override
    public void setConstantManager(ScriptConstantManager constantManager) {
        this.constantManager = constantManager;
    }

    /**
     * Function that is called when the script is evaluated.
     * 
     * @param script Script code itself
     */
    @Override
    public void eval(String script) {
        Value bindings = context.getBindings("js");

        // Add all custom functions and constants to our script.
        // It is important to check that the function and constant managers are set, 
        // because the user may not specify them and then we will get an error when trying to evaluate the script.
        
        if (functionManager != null) {
            for (ScriptFunction function : functionManager.getFunctions().values()) {
                bindings.putMember(function.getName(), new JsFunction(this, function));
            }
        }

        if (constantManager != null) {
            for (ScriptConstant constant : constantManager.getConstants().values()) {
                bindings.putMember(constant.getName(), constant.getValue());
            }
        }

        // Finally evaluating our script.
        context.eval("js", script);
    }
}
```

Now let's create a function:
```java
import exception.org.densy.scriptify.api.ScriptFunctionException;
import script.org.densy.scriptify.api.Script;
import function.script.org.densy.scriptify.api.ScriptFunction;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.Value;

public class JsFunction implements ProxyExecutable {

    private final Script script;
    private final ScriptFunction function;

    public JsFunction(Script script, ScriptFunction function) {
        this.script = script;
        this.function = function;
    }

    @Override
    public Object execute(Value... arguments) {
        // Convert Value... into Object[] so that we could pass arguments to our function:
        ScriptFunctionArgument[] args = new ScriptFunctionArgument[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = ScriptFunctionArgument.of(arguments[i].as(Object.class));
        }
        
        // Now let's call the function with arguments and handle possible exceptions:
        try {
            return function.invoke(script, args);
        } catch (ScriptFunctionException e) {
            throw new RuntimeException(e);
        }
    }
}
```

That's it! We have created JS scripts implementation using GraalVM. You can also write an implementation for any other language you want.