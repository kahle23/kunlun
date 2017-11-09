package saber.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Kahle
 */
public class PathUtils {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    private static String rootPath;
    private static String classPath;

    public static String getPackagePath(Class<?> clazz) {
        Package p = clazz.getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : "";
    }

    public static String getRootPath() throws URISyntaxException {
        if (StringUtils.isBlank(rootPath)) {
            String path = PathUtils.class.getResource("/").toURI().getPath();
            rootPath = new File(path).getParentFile().getParent();
        }
        return rootPath;
    }

    public static String getClassPath() throws URISyntaxException {
        if (StringUtils.isBlank(classPath)) {
            URL res = PathUtils.class.getClassLoader().getResource("");
            classPath = res != null ? new File(res.toURI().getPath()).toString() : "";
        }
        return classPath;
    }

    public static String getClassFilePath(Class<?> clazz) throws URISyntaxException {
        URI uri = clazz.getResource("").toURI();
        return new File(uri.getPath()).toString();
    }

}
