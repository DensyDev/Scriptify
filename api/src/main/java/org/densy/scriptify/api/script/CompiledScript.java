package org.densy.scriptify.api.script;

/**
 * Defines the structure of a compiled script that can be executed.
 *
 * @param <T> type of value returned by the script after evaluation
 */
public interface CompiledScript<T> extends AutoCloseable {

    /**
     * Retrieves the value produced by the script after compilation.
     *
     * @return the compiled script value
     */
    T get();

    /**
     * Executes the compiled script with the provided arguments.
     *
     * @param args arguments passed to the script during execution
     * @return the result of script execution
     */
    T eval(Object... args);

    /**
     * Closes the compiled script and releases any resources associated with it.
     */
    @Override
    void close();
}

