package artoria.beans;

/**
 * The bean map factory.
 * @author Kahle
 */
public interface BeanMapFactory {

    /**
     * Get the bean map instance.
     * @return The bean map instance
     */
    BeanMap getInstance();

    /**
     * Get the bean map instance.
     * @param bean The bean object
     * @return The bean map instance
     */
    BeanMap getInstance(Object bean);

}
