package artoria.file;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.Assert;
import artoria.util.PathUtils;
import artoria.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File factory.
 * @author Kahle
 */
public class FileFactory {
    private static final Map<String, Class<? extends BinaryFile>> BUCKET;
    private static Logger log = LoggerFactory.getLogger(FileFactory.class);

    static {
        BUCKET = new ConcurrentHashMap<String, Class<? extends BinaryFile>>();
        FileFactory.register("csv", Csv.class);
        FileFactory.register("txt", Txt.class);
        FileFactory.register("properties", PropertiesFile.class);
    }

    private static Class<? extends BinaryFile> getClassByExtension(String extensionString) {
        Assert.notBlank(extensionString, "Parameter \"extensionString\" must not blank. ");
        String extensionName = PathUtils.getExtension(extensionString);
        Assert.notBlank(extensionName, "Parameter \"extensionString\" have no extension. ");
        Class<? extends BinaryFile> fileClass = BUCKET.get(extensionName);
        Assert.notNull(fileClass
                , "The implementation type cannot be found based on the file extension. ");
        return fileClass;
    }

    public static Class<? extends BinaryFile> unregister(String extension) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        Class<? extends BinaryFile> remove = BUCKET.remove(extension);
        if (remove != null) {
            log.info("Unregister: \"" + extension + "\" to \"" + remove.getName() + "\". ");
        }
        return remove;
    }

    public static void register(String extension, Class<? extends BinaryFile> clazz) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        extension = extension.trim().toLowerCase();
        log.info("Register: \"" + extension + "\" to \"" + clazz.getName() + "\". ");
        BUCKET.put(extension, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinaryFile> T getInstance(Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        try {
            return (T) ReflectUtils.newInstance(clazz);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static <T extends TextFile> T getInstance(Class<T> clazz, Reader reader) {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        T fileEntity = FileFactory.getInstance(clazz);
        try {
            fileEntity.read(reader);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        return fileEntity;
    }

    public static <T extends BinaryFile> T getInstance(Class<T> clazz, InputStream inputStream) {

        return FileFactory.getInstance(clazz, inputStream, null);
    }

    public static <T extends BinaryFile> T getInstance(Class<T> clazz, InputStream inputStream, String charset) {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        T instance = FileFactory.getInstance(clazz);
        if (StringUtils.isNotBlank(charset)
                && instance instanceof TextFile) {
            ((TextFile) instance).setCharset(charset);
        }
        try {
            instance.read(inputStream);
            return instance;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static <T extends BinaryFile> T getInstance(File file) {

        return FileFactory.getInstance(null, file, null);
    }

    public static <T extends BinaryFile> T getInstance(File file, String charset) {

        return FileFactory.getInstance(null, file, charset);
    }

    public static <T extends BinaryFile> T getInstance(Class<T> clazz, File file) {

        return FileFactory.getInstance(clazz, file, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinaryFile> T getInstance(Class<T> clazz, File file, String charset) {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        Class<? extends BinaryFile> fileClass = clazz != null
                ? clazz : FileFactory.getClassByExtension(file.toString());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return (T) FileFactory.getInstance(fileClass, inputStream, charset);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static <T extends BinaryFile> T getInstance(String subpathInClasspath) {

        return FileFactory.getInstance(null, subpathInClasspath, null);
    }

    public static <T extends BinaryFile> T getInstance(String subpathInClasspath, String charset) {

        return FileFactory.getInstance(null, subpathInClasspath, charset);
    }

    public static <T extends BinaryFile> T getInstance(Class<T> clazz, String subpathInClasspath) {

        return FileFactory.getInstance(clazz, subpathInClasspath, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinaryFile> T getInstance(Class<T> clazz, String subpathInClasspath, String charset) {
        Assert.notBlank(subpathInClasspath, "Parameter \"subpathInClasspath\" must not blank. ");
        Class<? extends BinaryFile> fileClass = clazz != null
                ? clazz : FileFactory.getClassByExtension(subpathInClasspath);
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.findClasspath(subpathInClasspath);
            Assert.notNull(inputStream, "Parameter \"subpathInClasspath\" not found in classpath. ");
            return (T) FileFactory.getInstance(fileClass, inputStream, charset);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
