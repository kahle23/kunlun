package artoria.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static artoria.util.StringConstant.*;

/**
 * @author Kahle
 */
public class PathUtils {

    public static String subPath(File src, File parent) {
        String srcStr = src.toString();
        String parentStr = parent.toString();
        return PathUtils.subPath(srcStr, parentStr);
    }

    public static String subPath(String src, String parent) {
        Assert.notNull(src, "Source is required. ");
        Assert.notNull(parent, "Parent is required. ");
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

    public static final String REGEX_DOT = BACKLASH + DOT;
    public static String getPackagePath(Class<?> clazz) {
        Package p = clazz.getPackage();
        return p != null ? p.getName().replaceAll(REGEX_DOT, SLASH) : EMPTY_STRING;
    }

    private static String rootPath;
    public static String getRootPath() throws URISyntaxException {
        if (StringUtils.isNotBlank(rootPath)) { return rootPath; }
        String path = PathUtils.class.getResource(SLASH).toURI().getPath();
        rootPath = new File(path).getParentFile().getParent();
        return rootPath;
    }

    private static String classpath;
    public static String getClasspath() throws URISyntaxException {
        if (StringUtils.isNotBlank(classpath)) { return classpath; }
        URL res = PathUtils.class.getClassLoader().getResource(EMPTY_STRING);
        classpath = res != null ? new File(res.toURI().getPath()).toString() : EMPTY_STRING;
        return classpath;
    }

    public static String getClassFilePath(Class<?> clazz) throws URISyntaxException {
        URI uri = clazz.getResource(EMPTY_STRING).toURI();
        return new File(uri.getPath()).toString();
    }

}
