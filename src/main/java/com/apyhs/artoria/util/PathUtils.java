package com.apyhs.artoria.util;

import com.apyhs.artoria.exception.UncheckedException;

import java.io.File;
import java.net.URL;

import static com.apyhs.artoria.util.Const.*;

/**
 * Path tools.
 * @author Kahle
 */
public class PathUtils {
    private static final Class<?> THIS_CLASS = PathUtils.class;

    public static String getRootPath() {
        try {
            URL res = THIS_CLASS.getResource(SLASH);
            File path = res != null ? new File(res.toURI().getPath()) : null;
            return path != null ? path.getParentFile().getParent() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw new UncheckedException(e.getMessage(), e);
        }
    }

    public static String getClasspath() {
        try {
            URL res = ClassUtils.getDefaultClassLoader().getResource(EMPTY_STRING);
            return res != null ? new File(res.toURI().getPath()).toString() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw new UncheckedException(e.getMessage(), e);
        }
    }

    public static URL findJarClasspath(String name) {
        if (!name.startsWith(SLASH)) {
            name = SLASH + name;
        }
        return THIS_CLASS.getResource(name);
    }

    public static String subPath(File src, File parent) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(parent, "Parent must is not null. ");
        String srcStr = src.toString();
        String parentStr = parent.toString();
        return PathUtils.subPath(srcStr, parentStr);
    }

    public static String subPath(String src, String parent) {
        Assert.notBlank(src, "Source must is not blank. ");
        Assert.notBlank(parent, "Parent must is not blank. ");
        Assert.state(src.startsWith(parent), "Source must start with parent. ");
        return StringUtils.replace(src, parent, EMPTY_STRING);
    }

    public static String getExtension(String path) {
        if (path == null) { return EMPTY_STRING; }
        int extIndex = path.lastIndexOf(DOT);
        if (extIndex == -1) { return EMPTY_STRING; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > extIndex) { return EMPTY_STRING; }
        return path.substring(extIndex + 1);
    }

    public static String stripExtension(String path) {
        if (path == null) { return null; }
        int extIndex = path.lastIndexOf(DOT);
        if (extIndex == -1) { return path; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > extIndex) { return path; }
        return path.substring(0, extIndex);
    }

    public static String getPackagePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Package p = clazz.getPackage();
        String pName = p != null ? p.getName() : null;
        return pName != null ? StringUtils.replace(pName, DOT, SLASH) : EMPTY_STRING;
    }

    public static String getClassFilePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        try {
            URL res = clazz.getResource(EMPTY_STRING);
            String path = res != null ? res.toURI().getPath() : null;
            return path != null ? new File(path).toString() : EMPTY_STRING;
        }
        catch (Exception e) {
            throw new UncheckedException(e.getMessage(), e);
        }
    }

}
