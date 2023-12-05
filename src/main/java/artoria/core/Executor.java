package artoria.core;

/**
 * Provide the highest level of abstraction for executor.
 * @see java.util.concurrent.Executor
 * @author Kahle
 */
public interface Executor {

    /**
     * Executes the given task at some time in the future.
     * @param task The task to be performed
     */
    void execute(Object task);

}
