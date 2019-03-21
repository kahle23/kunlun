package artoria.util;

import artoria.exception.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import static artoria.common.Constants.BACKSLASH;
import static artoria.common.Constants.SLASH;

/**
 * ClassLoader tools.
 * @author Kahle
 */
public class ClassLoaderUtils {
    private static Logger log = Logger.getLogger(ClassLoaderUtils.class.getName());

    private static Enumeration<URL> getResources(ClassLoader classLoader, String resourceName) {
        if (classLoader == null) { return null; }
        try {
            return classLoader.getResources(resourceName);
        }
        catch (IOException e) {
            log.fine(ExceptionUtils.toString(e));
        }
        return null;
    }

    /**
     * Try to get the classloader.
     * @return Classloader gotten
     */
    public static ClassLoader getDefaultClassLoader() {
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = null;
        try {
            classLoader = currentThread.getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (classLoader != null) { return classLoader; }
        // No thread context class loader -> use class loader of this class.
        classLoader = ClassLoaderUtils.class.getClassLoader();
        if (classLoader != null) { return classLoader; }
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
        return classLoader;
    }

    /**
     * Load a given resource.
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     * @return The url of the resource
     */
    public static URL getResource(String resourceName, Class<?> callingClass) {
        Assert.notNull(resourceName
                , "Parameter \"resourceName\" must not null. ");
        resourceName = StringUtils.replace(resourceName, BACKSLASH, SLASH);
        boolean startsSlash = resourceName.startsWith(SLASH);
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader =
                currentThread.getContextClassLoader();
        URL url = classLoader.getResource(resourceName);
        if (url == null && startsSlash) {
            // Certain ClassLoaders need it without the leading "/".
            String subStr = resourceName.substring(1);
            url = classLoader.getResource(subStr);
        }
        classLoader = ClassLoaderUtils.class.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if (url == null) {
            url = classLoader.getResource(resourceName);
        }
        if (url == null && startsSlash) {
            // Certain ClassLoaders need it without the leading "/".
            String subStr = resourceName.substring(1);
            url = classLoader.getResource(subStr);
        }
        if (url == null) {
            classLoader = callingClass.getClassLoader();
            url = classLoader == null ? null
                    : classLoader.getResource(resourceName);
        }
        if (url == null) {
            url = callingClass.getResource(resourceName);
        }
        if (url == null && !startsSlash) {
            resourceName = SLASH + resourceName;
            return ClassLoaderUtils
                    .getResource(resourceName, callingClass);
        }
        return url;
    }

    /**
     * Load a given resources.
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     * @return A list of url for the resource
     */
    public static List<URL> getResources(String resourceName, Class<?> callingClass) {
        Assert.notNull(resourceName
                , "Parameter \"resourceName\" must not null. ");
        resourceName = StringUtils.replace(resourceName, BACKSLASH, SLASH);
        boolean startsSlash = resourceName.startsWith(SLASH);
        Thread currentThread = Thread.currentThread();
        List<URL> result = new ArrayList<URL>();
        ClassLoader classLoader =
                currentThread.getContextClassLoader();
        Enumeration<URL> urls =
                ClassLoaderUtils.getResources(classLoader, resourceName);
        if (CollectionUtils.isEmpty(urls) && startsSlash) {
            // Certain ClassLoaders need it without the leading "/".
            String subStr = resourceName.substring(1);
            urls = ClassLoaderUtils.getResources(classLoader, subStr);
        }
        classLoader = ClassLoaderUtils.class.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if (CollectionUtils.isEmpty(urls)) {
            urls = ClassLoaderUtils.getResources(classLoader, resourceName);
        }
        if (CollectionUtils.isEmpty(urls) && startsSlash) {
            // Certain ClassLoaders need it without the leading "/".
            String subStr = resourceName.substring(1);
            urls = ClassLoaderUtils.getResources(classLoader, subStr);
        }
        if (CollectionUtils.isEmpty(urls)) {
            classLoader = callingClass.getClassLoader();
            urls = ClassLoaderUtils.getResources(classLoader, resourceName);
        }
        if (CollectionUtils.isEmpty(urls)) {
            URL url = callingClass.getResource(resourceName);
            if (url != null) { result.add(url); }
        }
        CollectionUtils.addAll(result, urls);
        if (CollectionUtils.isEmpty(result) && !startsSlash) {
            resourceName = SLASH + resourceName;
            return ClassLoaderUtils.getResources(resourceName, callingClass);
        }
        return result;
    }

    /**
     * This is a convenience method to load a resource as a stream.
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     * @return The stream of loaded resource
     */
    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = ClassLoaderUtils.getResource(resourceName, callingClass);
        try {
            return url != null ? url.openStream() : null;
        }
        catch (IOException e) {
            log.severe(ExceptionUtils.toString(e));
        }
        return null;
    }

    /**
     * Load a class with a given name.
     * @param className The name of the class to load
     * @param callingClass The Class object of the calling object
     * @return Class that are loaded
     * @throws ClassNotFoundException If the class cannot be found anywhere
     */
    public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = currentThread.getContextClassLoader();
        try {
            if (classLoader != null) {
                return classLoader.loadClass(className);
            }
        }
        catch (ClassNotFoundException e) {
            log.fine(ExceptionUtils.toString(e));
        }
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            classLoader = ClassLoaderUtils.class.getClassLoader();
            try {
                if (classLoader != null) {
                    return classLoader.loadClass(className);
                }
            }
            catch (ClassNotFoundException ex) {
                classLoader = callingClass != null
                        ? callingClass.getClassLoader() : null;
                if (classLoader != null) {
                    return classLoader.loadClass(className);
                }
            }
            log.fine(ExceptionUtils.toString(e));
            throw e;
        }
    }

}
