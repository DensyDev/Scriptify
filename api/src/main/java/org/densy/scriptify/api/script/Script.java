package org.densy.scriptify.api.script;

import org.densy.scriptify.api.exception.ScriptException;
import org.densy.scriptify.api.exception.ScriptFunctionException;
import org.densy.scriptify.api.script.constant.ScriptConstantManager;
import org.densy.scriptify.api.script.function.ScriptFunctionManager;
import org.densy.scriptify.api.script.security.ScriptSecurityManager;

/**
 * Defines the structure of a script that can be executed.
 *
 * @param <T> Type of value returned by the script after evaluation
 */
public interface Script<T> {

    /**
     * Retrieves the security manager associated with this script.
     *
     * @return The ScriptSecurityManager for this script
     * @see ScriptSecurityManager
     */
    ScriptSecurityManager getSecurityManager();

    /**
     * Retrieves the function manager associated with this script.
     *
     * @return The ScriptFunctionManager for this script
     * @see ScriptFunctionManager
     */
    ScriptFunctionManager getFunctionManager();

    /**
     * Sets the function manager for this script.
     *
     * @param functionManager The manager handling script functions
     * @see ScriptFunctionManager
     */
    void setFunctionManager(ScriptFunctionManager functionManager);

    /**
     * Retrieves the constant manager associated with this script.
     *
     * @return The ScriptConstantManager for this script
     * @see ScriptConstantManager
     */
    ScriptConstantManager getConstantManager();

    /**
     * Sets the constant manager for this script
     *
     * @param constantManager The manager handling script constants
     * @see ScriptConstantManager
     */
    void setConstantManager(ScriptConstantManager constantManager);

    /**
     * Add an extra script on top of the whole script.
     *
     * @param script Extra script
     */
    void addExtraScript(String script);

    /**
     * Compiles the script.
     *
     * @param script the script to compile
     * @return compiled script object
     * @throws ScriptException if there's an error during script compiling
     */
    CompiledScript<T> compile(String script) throws ScriptException;

    /**
     * One-time evaluation of a script without worrying about closing the context.
     *
     * @param script the script to evaluate
     * @return the evaluation result
     * @throws ScriptException if there's an error during script evaluation
     */
    T evalOneShot(String script) throws ScriptException;

    /**
     * Evaluates and executes the script.
     *
     * @param script the script to evaluate
     * @return the evaluation result
     * @throws ScriptFunctionException if there's an error during script evaluation
     * @deprecated For a one-time evaluation of a script, use the Script#evalOneShot method;
     *             for pre-compiling a script, use Script#compile.
     */
    @Deprecated
    default T eval(String script) throws ScriptException {
        return this.evalOneShot(script);
    }
}
