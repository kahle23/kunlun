package artoria.polyglot;

/**
 * The polyglot execution provider.
 * @author Kahle
 */
public interface PolyglotProvider {

    /**
     * Executes the code.
     * @param name The language name
     * @param source The source code
     * @param config The config (most scenarios are Map, nullable)
     * @param data The data to be used for code execution
     * @return The value returned by the code
     */
    Object eval(String name, Object source, Object config, Object data);

    /**
     * Used to call functions in code.
     * @param name The language name
     * @param source The source code
     * @param config The config (most scenarios are Map, nullable)
     * @param function The name of the function to call
     * @param arguments The arguments to pass to the function
     * @return The value returned by the function
     */
    Object invoke(String name, Object source, Object config, String function, Object... arguments);

}
