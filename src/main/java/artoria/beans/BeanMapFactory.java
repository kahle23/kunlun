package artoria.beans;

/**
 * The bean map factory.
 * @author Kahle
 */
public interface BeanMapFactory {

    /**
     * Get the bean map instance.
     * @param bean The bean object (nullable)
     * @return The bean map instance
     */
    BeanMap getInstance(Object bean);

}
