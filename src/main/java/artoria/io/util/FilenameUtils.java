package artoria.io.util;

import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.File;
import java.net.URL;

import static artoria.common.Constants.*;

/**
 * The filename and filepath tools.
 * @author Kahle
 */
public class FilenameUtils {
    private static final Class<?> THIS_CLASS = FilenameUtils.class;
    private static final char EXTENSION_SEPARATOR = '.';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char UNIX_SEPARATOR = '/';

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
     * Returns the index of the last directory separator character.
     * @param filename The filename to find the last path separator in, null returns -1
     * @return The index of the last directory separator character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) { return MINUS_ONE; }
        int lastWindowsIndex = filename.lastIndexOf(WINDOWS_SEPARATOR);
        int lastUnixIndex = filename.lastIndexOf(UNIX_SEPARATOR);
        return Math.max(lastUnixIndex, lastWindowsIndex);
    }

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * @param filename The filename to find the last path separator in, null returns -1
     * @return The index of the last extension separator character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) { return MINUS_ONE; }
        int extensionIndex = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int separatorIndex = indexOfLastSeparator(filename);
        return separatorIndex > extensionIndex ? MINUS_ONE : extensionIndex;
    }

    /**
     * Gets the name minus the path from a full filename.
     * @param filename The filename to query, null returns null
     * @return The name of the file without the path
     */
    public static String getName(String filename) {
        if (filename == null) { return null; }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + ONE);
    }

    /**
     * Gets the extension of a filename.
     * @param filename The filename to retrieve the extension of
     * @return The extension of the file
     */
    public static String getExtension(String filename) {
        if (filename == null) { return null; }
        int index = indexOfExtension(filename);
        if (index == MINUS_ONE) { return EMPTY_STRING; }
        return filename.substring(index + ONE);
    }

    /**
     * Removes the extension from a filename.
     * @param filename The filename to query, null returns null
     * @return The filename minus the extension
     */
    public static String removeExtension(String filename) {
        if (filename == null) { return null; }
        int index = indexOfExtension(filename);
        if (index == MINUS_ONE) { return filename; }
        return filename.substring(ZERO, index);
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
