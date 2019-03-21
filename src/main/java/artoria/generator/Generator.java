package artoria.generator;

/**
 * Provide the highest level of abstraction for Generator.
 * @param <T> The return value after generate execution
 * @author Kahle
 */
public interface Generator<T> {

    /**
     * Execute the generation logic.
     * @return Generated result or generated status
     * @throws GenerateException Generate error or other error
     */
    T generate() throws GenerateException;

}
