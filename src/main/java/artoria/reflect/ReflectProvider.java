package artoria.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * Reflect provider.
 * @author Kahle
 */
public interface ReflectProvider {

    /**
     * Match declared types to actual types.
     * @param declaredTypes The method declared types
     * @param actualTypes The invoker input types
     * @return True is matched, false is not
     */
    boolean matchTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes);

    /**
     * Check accessible.
     * @param accessible A checked accessible object
     * @return True is accessible, false is not
     */
    boolean checkAccessible(AccessibleObject accessible);

    /**
     * Make accessible.
     * @param accessible A operated accessible object
     */
    void makeAccessible(AccessibleObject accessible);

    /**
     * Find parameters types.
     * @param params Parameters
     * @return The parameters's types
     */
    Class<?>[] findTypes(Object[] params);

    /**
     * Find all constructors.
     * @param clazz The class will be find
     * @param <T> The type of object to construct
     * @return The constructors array
     */
    <T> Constructor<T>[] findConstructors(Class<T> clazz);

    /**
     * Find constructor and type similar.
     * @param clazz The class will be find
     * @param parameterTypes Parameter types
     * @param <T> The type of object to construct
     * @return The constructor
     * @throws NoSuchMethodException No such method
     */
    <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Find all public fields.
     * @param clazz The class will be find
     * @return The fields array
     */
    Field[] findFields(Class<?> clazz);

    /**
     * Find all declared fields.
     * @param clazz The class will be find
     * @return The fields array
     */
    Field[] findDeclaredFields(Class<?> clazz);

    /**
     * Find all access fields.
     * @param clazz The class will be find
     * @return The fields array
     */
    Field[] findAccessFields(Class<?> clazz);

    /**
     * Find field.
     * @param clazz The class will be find
     * @param fieldName Field name
     * @return The field
     * @throws NoSuchFieldException No such field
     */
    Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException;

    /**
     * Find all public methods.
     * @param clazz The class will be find
     * @return The methods array
     */
    Method[] findMethods(Class<?> clazz);

    /**
     * Find all declared methods.
     * @param clazz The class will be find
     * @return The methods array
     */
    Method[] findDeclaredMethods(Class<?> clazz);

    /**
     * Find all access methods.
     * @param clazz The class will be find
     * @return The methods array
     */
    Method[] findAccessMethods(Class<?> clazz);

    /**
     * Find method.
     * @param clazz The class will be find
     * @param methodName Method name
     * @param parameterTypes parameter types
     * @return The method
     * @throws NoSuchMethodException No such method
     */
    Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Find similar method.
     * @param clazz The class will be find
     * @param methodName Method name
     * @param parameterTypes parameter types
     * @return The method
     * @throws NoSuchMethodException No such method
     */
    Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Find all property descriptors.
     * @param clazz The class will be find
     * @return The property descriptors
     */
    PropertyDescriptor[] findPropertyDescriptors(Class<?> clazz);

    /**
     * Create object by reflection.
     * @param clazz The type of object to create
     * @param args Parameters to the constructor
     * @param <T> The type of object to construct
     * @return The object created
     */
    <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException;
    // TODO: When JDK 1.7

}
