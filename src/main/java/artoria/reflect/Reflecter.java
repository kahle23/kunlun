package artoria.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Reflect tools.
 * @author Kahle
 */
public interface Reflecter {

    /**
     * Find class by name.
     * @param className Class name
     * @return Class
     * @throws ClassNotFoundException Class not found
     */
    Class<?> forName(String className) throws ClassNotFoundException;

    /**
     * Find class by name.
     * @param className Class name
     * @param initialize Is initialize
     * @param loader Class loader
     * @return Class
     * @throws ClassNotFoundException Class not found
     */
    Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException;

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
     * Find all constructors.
     * @param clazz The class will be find
     * @return Constructors array
     */
    Constructor<?>[] findConstructors(Class<?> clazz);

    /**
     * Find constructor and type similar.
     * @param clazz The class will be find
     * @param parameterTypes parameter types
     * @return The constructor
     * @throws NoSuchMethodException No such method
     */
    Constructor<?> findConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException;

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
