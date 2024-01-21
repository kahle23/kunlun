package artoria.bean;

import java.util.Map;

/**
 * The bean manager. It's just a bean container.
 * It's just a container that makes it easy for the caller to get some beans.
 *
 * It won't take on any factory related duties.
 * If you want to implement an IOC or DI container like the spring container,
 * You can have your "BeanFactory" or "BeanProvider" hold the "BeanManager".
 *
 * Why implement this?
 * Because in some scenarios containers are a convenient way to do it.
 * IOC or DI are not needed in these scenarios because they are too heavy.
 *
 * Because generic type erasure, and it's positioning are just container
 * (there is no need to store bean definitions), so just need "Class" instead of "Type".
 * The bean definitions are required only when building an instance, not after.
 *
 * @author Kahle
 */
public interface BeanManager {

    /**
     * Does this bean manager contain a bean instance with the given name?
     * @param name The name of the bean to query
     * @return Whether a bean with the given name is present
     */
    boolean contains(String name);

    /**
     * Remove the bean instance based on the name.
     * @param name The name of the bean to remove
     * @return The removed bean instance or null
     */
    Object remove(String name);

    /**
     * Get the bean instance by name.
     * @param name The name of the bean to query
     * @return The bean instance or null
     */
    Object get(String name);

    /**
     * Get the bean instance by type (only one bean of this type can exist).
     * @param type The type the bean must match, can be an interface or superclass
     * @param <T> The generic type corresponding to the type
     * @return The bean instance or null (maybe throw exception)
     */
    <T> T get(Class<T> type);

    /**
     * Get the bean instance by name and type (the type of the bean must match).
     * @param name The name of the bean to query
     * @param type The type the bean must match, can be an interface or superclass
     * @param <T> The generic type corresponding to the type
     * @return The bean instance or null (maybe throw exception)
     */
    <T> T get(String name, Class<T> type);

    /**
     * Put the bean instance by name.
     *  If the name already exists, an exception is usually thrown.
     * @param name The name of the bean instance (name must be unique)
     * @param bean The bean instance to be put
     * @return The old bean instance or null (maybe throw exception)
     */
    Object put(String name, Object bean);

    /**
     * Return the aliases for the given bean name, if any.
     * @param name The bean name to check for aliases
     * @return The aliases, or an empty array if none
     */
    String[] getAliases(String name);

    /**
     * Get the names of beans matching the given type (including subclasses).
     * @param type The class or interface to match, or null for all bean names
     * @return The names of beans, or an empty array if none
     */
    String[] getNames(Class<?> type);

    /**
     * Get the bean instances that match the given type (including subclasses).
     * @param type The class or interface to match, or null for all concrete beans
     * @param <T> The generic type corresponding to the type
     * @return The matching beans, bean names as keys, bean instances as values
     */
    <T> Map<String, T> getBeans(Class<T> type);

}
