package artoria.lifecycle;

/**
 * Represents that the object needs to be destroyed.
 * @author Kahle
 */
public interface Destroyable {

    /**
     * Destroy this object.
     * @throws Exception Maybe occur exception
     */
    void destroy() throws Exception;

}
