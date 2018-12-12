package artoria.generator;

/**
 * Generator interface.
 * @author Kahle
 */
public interface Generator {

    /**
     * Execute the generation logic.
     * @throws GenerateException Generate error or other error
     */
    void generate() throws GenerateException;

}
