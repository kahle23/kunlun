package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.exception.ExceptionUtils;
import com.github.kahlkn.artoria.time.DateUtils;

import java.io.File;
import java.net.URL;

import static com.github.kahlkn.artoria.util.Const.*;

/**
 * Path tools.
 * @author Kahle
 */
public class PathUtils {
    private static final Class<?> THIS_CLASS = PathUtils.class;

    /**
     *
     * @return
     */
    public static String getRootPath() {
        try {
            URL res = THIS_CLASS.getResource(SLASH);
            File path = res != null ? new File(res.toURI().getPath()) : null;
            return path != null ? path.getParentFile().getParent() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     *
     * @return
     */
    public static String getClasspath() {
        try {
            URL res = ClassUtils.getDefaultClassLoader().getResource(EMPTY_STRING);
            return res != null ? new File(res.toURI().getPath()).toString() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static URL findJarClasspath(String name) {
        if (!name.startsWith(SLASH)) {
            name = SLASH + name;
        }
        return THIS_CLASS.getResource(name);
    }

    /**
     *
     * @param src
     * @param parent
     * @return
     */
    public static String subPath(File src, File parent) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(parent, "Parent must is not null. ");
        String srcStr = src.toString();
        String parentStr = parent.toString();
        return PathUtils.subPath(srcStr, parentStr);
    }

    /**
     *
     * @param src
     * @param parent
     * @return
     */
    public static String subPath(String src, String parent) {
        Assert.notBlank(src, "Source must is not blank. ");
        Assert.notBlank(parent, "Parent must is not blank. ");
        Assert.state(src.startsWith(parent), "Source must start with parent. ");
        return StringUtils.replace(src, parent, EMPTY_STRING);
    }

    /**
     *
     * @param path
     * @return
     */
    public static String getExtension(String path) {
        if (path == null) { return null; }
        int dotIndex = path.lastIndexOf(DOT);
        if (dotIndex == -1) { return EMPTY_STRING; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > dotIndex) { return EMPTY_STRING; }
        return path.substring(dotIndex + 1);
    }

    /**
     *
     * @param path
     * @return
     */
    public static String stripExtension(String path) {
        if (path == null) { return null; }
        int extIndex = path.lastIndexOf(DOT);
        if (extIndex == -1) { return path; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > extIndex) { return path; }
        return path.substring(0, extIndex);
    }

    /**
     *
     * @param path
     * @return
     */
    public static String notRepeatPath(String path) {
        if (path == null) { return null; }
        while (new File(path).exists()) {
            String body = PathUtils.stripExtension(path);
            String ext = PathUtils.getExtension(path);
            path = body + UNDERLINE + DateUtils.getTimestamp() + DOT + ext;
        }
        return path;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String getPackagePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Package p = clazz.getPackage();
        String pName = p != null ? p.getName() : null;
        return pName != null ? StringUtils.replace(pName, DOT, SLASH) : EMPTY_STRING;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        try {
            URL res = clazz.getResource(EMPTY_STRING);
            String path = res != null ? res.toURI().getPath() : null;
            return path != null ? new File(path).toString() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
