package artoria.lifecycle;

/**
 * Represents that the object needs to be initialized.
 * @author Kahle
 */
public interface Initializable {

    /**
     * Initialize this object.
     * @throws LifecycleException Maybe occur exception
     */
    void initialize() throws LifecycleException;

}
