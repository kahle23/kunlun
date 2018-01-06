package com.apyhs.artoria.util;

import com.apyhs.artoria.exception.UncheckedException;

import java.io.File;
import java.net.URL;

/**
 * Path tools.
 * @author Kahle
 */
public class PathUtils {

    protected static String getRootPath() {
        try {
            URL res = PathUtils.class.getResource(StringConstant.SLASH);
            File path = res != null ? new File(res.toURI().getPath()) : null;
            return path != null ? path.getParentFile().getParent() : StringConstant.EMPTY_STRING;
        }
        catch (Throwable t) {
            throw new UncheckedException(t.getMessage(), t);
        }
    }

    protected static String getClasspath() {
        try {
            URL res = ClassUtils.getDefaultClassLoader().getResource(StringConstant.EMPTY_STRING);
            return res != null ? new File(res.toURI().getPath()).toString() : StringConstant.EMPTY_STRING;
        }
        catch (Throwable t) {
            throw new UncheckedException(t.getMessage(), t);
        }
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
        return StringUtils.replace(src, parent, StringConstant.EMPTY_STRING);
    }

    public static String getExtension(String path) {
        if (path == null) { return StringConstant.EMPTY_STRING; }
        int extIndex = path.lastIndexOf(StringConstant.DOT);
        if (extIndex == -1) { return StringConstant.EMPTY_STRING; }
        int folderIndex = path.lastIndexOf(StringConstant.FILE_SEPARATOR);
        if (folderIndex > extIndex) { return StringConstant.EMPTY_STRING; }
        return path.substring(extIndex + 1);
    }

    public static String stripExtension(String path) {
        if (path == null) { return null; }
        int extIndex = path.lastIndexOf(StringConstant.DOT);
        if (extIndex == -1) { return path; }
        int folderIndex = path.lastIndexOf(StringConstant.FILE_SEPARATOR);
        if (folderIndex > extIndex) { return path; }
        return path.substring(0, extIndex);
    }

    public static String getPackagePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Package p = clazz.getPackage();
        String pName = p != null ? p.getName() : null;
        return pName != null ? StringUtils.replace(pName, StringConstant.DOT, StringConstant.SLASH) : StringConstant.EMPTY_STRING;
    }

    public static String getClassFilePath(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        try {
            URL res = clazz.getResource(StringConstant.EMPTY_STRING);
            String path = res != null ? res.toURI().getPath() : null;
            return path != null ? new File(path).toString() : StringConstant.EMPTY_STRING;
        }
        catch (Throwable t) {
            throw new UncheckedException(t.getMessage(), t);
        }
    }

}
