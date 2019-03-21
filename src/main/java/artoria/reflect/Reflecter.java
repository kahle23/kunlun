package artoria.reflect;

import java.lang.reflect.*;
import java.util.Map;

/**
 * Reflect tools.
 * @author Kahle
 */
public interface Reflecter {

    /**
     * Find parameters types.
     * @param parameters Parameters
     * @return The parameters's types
     */
    Class<?>[] findParameterTypes(Object... parameters);

    /**
     * Match declared types to actual types.
     * @param declaredTypes The method declared types
     * @param actualTypes The invoker input types
     * @return True is matched, false is not
     */
    boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes);

    /**
     * Check accessible.
     * @param accessible Be checked object
     * @param <T> Constraint input must is AccessibleObject's subclass
     * @return True is accessible, false is not
     */
    <T extends AccessibleObject> boolean checkAccessible(T accessible);

    /**
     * Make accessible.
     * @param accessible Be made object
     * @param <T> Constraint input must is AccessibleObject's subclass
     */
    <T extends AccessibleObject> void makeAccessible(T accessible);

    /**
     * Create object by reflection.
     * @param clazz The type of object to create
     * @param args Parameters to the constructor
     * @param <T> The type of object to construct
     * @return Objects created
     */
    <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException;
    // TODO: When JDK 1.7

    /**
     * Find all constructors.
     * @param clazz The class will be find
     * @param <T> The type of object to construct
     * @return Constructors array
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
     * @return Fields array
     */
    Field[] findFields(Class<?> clazz);

    /**
     * Find all declared fields.
     * @param clazz The class will be find
     * @return Fields array
     */
    Field[] findDeclaredFields(Class<?> clazz);

    /**
     * Find all access fields.
     * @param clazz The class will be find
     * @return Fields array
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
     * @return Methods array
     */
    Method[] findMethods(Class<?> clazz);

    /**
     * Find all declared methods.
     * @param clazz The class will be find
     * @return Methods array
     */
    Method[] findDeclaredMethods(Class<?> clazz);

    /**
     * Find all access methods.
     * @param clazz The class will be find
     * @return Methods array
     */
    Method[] findAccessMethods(Class<?> clazz);

    /**
     * Find all read methods (getter).
     * @param clazz The class will be find
     * @return Methods map (key is method name)
     */
    Map<String, Method> findReadMethods(Class<?> clazz);

    /**
     * Find all write methods (setter).
     * @param clazz The class will be find
     * @return Methods map (key is method name)
     */
    Map<String, Method> findWriteMethods(Class<?> clazz);

    /**
     * Find method.
     * @param clazz The class will be find
     * @param methodName Method name
     * @param parameterTypes parameter types
     * @return The Method
     * @throws NoSuchMethodException No such method
     */
    Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Find similar method.
     * @param clazz The class will be find
     * @param methodName Method name
     * @param parameterTypes parameter types
     * @return The Method
     * @throws NoSuchMethodException No such method
     */
    Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

}
