package saber;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PathUtils {
    public static final String FOLDER_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final char EXTENSION_SEPARATOR = '.';

    private static String rootPath;
    private static String classPath;

    public static String getName(File path) {
        return path.getName();
    }

    public static String getExtension(String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return path.substring(extIndex + 1);
    }

    public static String stripExtension(String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }
        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }
        return path.substring(0, extIndex);
    }

    public static boolean isAbsolute(File path) {
        return path.isAbsolute();
    }

    public static String absolutePath(File path) {
        return path.getAbsolutePath();
    }

    public static String canonicalPath(File path) throws IOException {
        return path.getCanonicalPath();
    }

    public static String packagePath(Class<?> clazz) {
        Package p = clazz.getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : "";
    }

    public static String clazzPath(Class<?> clazz) throws URISyntaxException {
        URI uri = clazz.getResource("").toURI();
        return new File(uri.getPath()).toString();
    }

    public static String rootPath() throws URISyntaxException {
        if (!StringUtils.hasText(rootPath)) {
            String path = PathUtils.class.getResource("/").toURI().getPath();
            rootPath = new File(path).getParentFile().getParent();
        }
        return rootPath;
    }

    public static String classPath() throws URISyntaxException {
        if (!StringUtils.hasText(classPath)) {
            URL res = PathUtils.class.getClassLoader().getResource("");
            classPath = res != null ? new File(res.toURI().getPath()).toString() : "";
        }
        return classPath;
    }

}
