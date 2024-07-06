/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * The reflection service.
 * @author Kahle
 */
public interface ReflectService {

    /**
     * Make accessible.
     * @param accessible An operated accessible object
     */
    void makeAccessible(AccessibleObject accessible);

    /**
     * Check accessible.
     * @param accessible A checked accessible object
     * @return True is accessible, false is not
     */
    boolean checkAccessible(AccessibleObject accessible);

    /**
     * Gets all constructors.
     * @param clazz The class to be accessed
     * @param <T> The type of object to construct
     * @return An array of constructors that was obtained
     */
    <T> Constructor<T>[] getConstructors(Class<T> clazz);

    /**
     * Gets a constructor of similar type.
     * @param clazz The class to be accessed
     * @param parameterTypes The parameter type of the constructor
     * @param <T> The type of object to construct
     * @return The constructor that was obtained
     * @throws NoSuchMethodException The constructor could not be found
     */
    <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Gets all the public fields.
     * @param clazz The class to be accessed
     * @return An array of fields
     */
    Field[] getFields(Class<?> clazz);

    /**
     * Gets all the declared fields.
     * @param clazz The class to be accessed
     * @return An array of fields
     */
    Field[] getDeclaredFields(Class<?> clazz);

    /**
     * Gets all the accessible fields.
     * @param clazz The class to be accessed
     * @return An array of fields
     */
    Field[] getAccessibleFields(Class<?> clazz);

    /**
     * Gets the field by the field name.
     * @param clazz The class to be accessed
     * @param fieldName The field name
     * @return The field that was obtained
     * @throws NoSuchFieldException The field could not be found
     */
    Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException;

    /**
     * Gets all the public methods.
     * @param clazz The class to be accessed
     * @return An array of methods
     */
    Method[] getMethods(Class<?> clazz);

    /**
     * Gets all the declared methods.
     * @param clazz The class to be accessed
     * @return An array of methods
     */
    Method[] getDeclaredMethods(Class<?> clazz);

    /**
     * Gets all the accessible methods.
     * @param clazz The class to be accessed
     * @return An array of methods
     */
    Method[] getAccessibleMethods(Class<?> clazz);

    /**
     * Gets the method by the method name and the parameter types.
     * @param clazz The class to be accessed
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method that was obtained
     * @throws NoSuchMethodException The method could not be found
     */
    Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Gets the similar method by the method name and the parameter types.
     * @param clazz The class to be accessed
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method that was obtained
     * @throws NoSuchMethodException The method could not be found
     */
    Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException;

    /**
     * Gets all the property descriptors.
     * @param clazz The class to be accessed
     * @return An array of property descriptors
     */
    PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz);

    /**
     * Create object by reflection.
     * @param clazz The type of object to create
     * @param args The arguments to the constructor
     * @param <T> The type of object to create
     * @return The object that was created
     */
    <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException;
    // TODO: When JDK 1.7

}
