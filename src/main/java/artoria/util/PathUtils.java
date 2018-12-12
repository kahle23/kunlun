package artoria.util;

import java.io.File;
import java.net.URL;

import static artoria.common.Constants.*;

/**
 * Path tools.
 * @author Kahle
 */
public class PathUtils {
    private static final Class<?> THIS_CLASS = PathUtils.class;

    /**
     *
     * @param subpath
     * @return
     */
    public static String findClasspath(String subpath) {
        Assert.notBlank(subpath, "Parameter \"subpath\" must not blank. ");
        if (!subpath.startsWith(SLASH)) {
            subpath = SLASH + subpath;
        }
        URL res = THIS_CLASS.getResource(subpath);
        File file = res != null ? new File(res.getFile()) : null;
        return file != null ? file.toString() : null;
    }

    /**
     *
     * @return
     */
    public static String getRootPath() {
        URL res = THIS_CLASS.getResource(SLASH);
        File file = res != null ? new File(res.getFile()) : null;
        file = file != null ? file.getParentFile() : null;
        return file != null ? file.getParent() : null;
    }

    /**
     *
     * @return
     */
    public static String getClasspath() {
        URL res = THIS_CLASS.getResource(SLASH);
        File file = res != null ? new File(res.getFile()) : null;
        return file != null ? file.toString() : null;
    }

    /**
     *
     * @param source
     * @param parent
     * @return
     */
    public static String subPath(File source, File parent) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(parent, "Parameter \"parent\" must not null. ");
        String sourceStr = source.toString();
        String parentStr = parent.toString();
        return PathUtils.subPath(sourceStr, parentStr);
    }

    /**
     *
     * @param source
     * @param parent
     * @return
     */
    public static String subPath(String source, String parent) {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        Assert.notBlank(parent, "Parameter \"parent\" must not blank. ");
        Assert.state(source.startsWith(parent)
                , "Parameter \"source\" must start with parameter \"parent\". ");
        return StringUtils.replace(source, parent, EMPTY_STRING);
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
     * @param clazz
     * @return
     */
    public static String getPackagePath(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Package p = clazz.getPackage();
        String pName = p != null ? p.getName() : null;
        return pName != null ? StringUtils.replace(pName, DOT, SLASH) : null;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        URL res = clazz.getResource(EMPTY_STRING);
        File file = res != null ? new File(res.getFile()) : null;
        return file != null ? file.toString() : null;
    }

}
