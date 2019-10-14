package artoria.file;

import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.File;
import java.net.URL;

import static artoria.common.Constants.*;

/**
 * Filename and filepath tools.
 * @author Kahle
 */
public class FilenameUtils {
    private static final Class<?> THIS_CLASS = FilenameUtils.class;

    /**
     * Get the root path of the current project.
     * @return The root path of the current project
     */
    public static String getRootPath() {
        URL res = THIS_CLASS.getResource(SLASH);
        File file = res != null ? new File(res.getFile()) : null;
        file = file != null ? file.getParentFile() : null;
        return file != null ? file.getParent() : null;
    }

    /**
     * Get the classpath of the current project.
     * @return Classpath of the current project
     */
    public static String getClasspath() {
        URL res = THIS_CLASS.getResource(SLASH);
        File file = res != null ? new File(res.getFile()) : null;
        return file != null ? file.toString() : null;
    }

    /**
     * Get the sub path from the source path and the corresponding parent path.
     * @param source The source path
     * @param parent The parent path
     * @return The sub path
     */
    public static String subPath(File source, File parent) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(parent, "Parameter \"parent\" must not null. ");
        String sourceStr = source.toString();
        String parentStr = parent.toString();
        return FilenameUtils.subPath(sourceStr, parentStr);
    }

    /**
     * Get the sub path from the source path and the corresponding parent path.
     * @param source The source path
     * @param parent The parent path
     * @return The sub path
     */
    public static String subPath(String source, String parent) {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        Assert.notBlank(parent, "Parameter \"parent\" must not blank. ");
        Assert.state(source.startsWith(parent)
                , "Parameter \"source\" must start with parameter \"parent\". ");
        return StringUtils.replace(source, parent, EMPTY_STRING);
    }

    /**
     * Get the extension from the path.
     * @param path Input path
     * @return The extension name
     */
    public static String getExtension(String path) {
        if (path == null) { return null; }
        int dotIndex = path.lastIndexOf(DOT);
        if (dotIndex == MINUS_ONE) { return EMPTY_STRING; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > dotIndex) { return EMPTY_STRING; }
        return path.substring(dotIndex + ONE);
    }

    /**
     * Get the path without extensions.
     * @param path Input path
     * @return Path without extension
     */
    public static String removeExtension(String path) {
        if (path == null) { return null; }
        int extIndex = path.lastIndexOf(DOT);
        if (extIndex == MINUS_ONE) { return path; }
        int folderIndex = path.lastIndexOf(FILE_SEPARATOR);
        if (folderIndex > extIndex) { return path; }
        return path.substring(ZERO, extIndex);
    }

    /**
     * Get the package path of the class.
     * @param clazz Input class
     * @return The package path
     */
    public static String getPackagePath(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Package p = clazz.getPackage();
        String pName = p != null ? p.getName() : null;
        return pName != null ? StringUtils.replace(pName, DOT, SLASH) : null;
    }

    /**
     * Get the path of the class file.
     * @param clazz Input class
     * @return The class file path
     */
    public static String getClassFilePath(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        URL res = clazz.getResource(EMPTY_STRING);
        File file = res != null ? new File(res.getFile()) : null;
        return file != null ? file.toString() : null;
    }

}
