package artoria.file;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.Assert;
import artoria.util.PathUtils;
import artoria.util.StringUtils;

import java.io.File;
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
    }

    public static Class<? extends BinaryFile> unregister(String extension) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        Class<? extends BinaryFile> remove = BUCKET.remove(extension);
        if (remove != null) {
            log.info("Unregister: " + extension + " >> " + remove.getName());
        }
        return remove;
    }

    public static void register(String extension, Class<? extends BinaryFile> clazz) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        extension = extension.trim().toLowerCase();
        log.info("Register: " + extension + " >> " + clazz.getName());
        BUCKET.put(extension, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinaryFile> T getInstance(String extension) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        extension = extension.trim().toLowerCase();
        try {
            Class<? extends BinaryFile> fileClass = BUCKET.get(extension);
            Assert.notNull(fileClass
                    , "The implementation type cannot be found based on the file extension. ");
            return (T) ReflectUtils.newInstance(fileClass, extension);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static <T extends TextFile> T getInstance(String extension, Reader reader) {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        T fileEntity = FileFactory.getInstance(extension);
        try {
            fileEntity.read(reader);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        return fileEntity;
    }

    public static <T extends BinaryFile> T getInstance(String extension, InputStream inputStream) {

        return FileFactory.getInstance(extension, inputStream, null);
    }

    public static <T extends BinaryFile> T getInstance(String extension, InputStream inputStream, String charset) {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        T fileEntity = FileFactory.getInstance(extension);
        if (StringUtils.isNotBlank(charset)
                && fileEntity instanceof TextFile) {
            ((TextFile) fileEntity).setCharset(charset);
        }
        try {
            fileEntity.read(inputStream);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        return fileEntity;
    }

    public static <T extends BinaryFile> T getInstance(File file) {

        return FileFactory.getInstance(null, file, null);
    }

    public static <T extends BinaryFile> T getInstance(File file, String charset) {

        return FileFactory.getInstance(null, file, charset);
    }

    public static <T extends BinaryFile> T getInstance(String extension, File file) {

        return FileFactory.getInstance(extension, file, null);
    }

    public static <T extends BinaryFile> T getInstance(String extension, File file, String charset) {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        String fileExt = PathUtils.getExtension(file.toString());
        extension = StringUtils.isNotBlank(fileExt) ? fileExt : extension;
        Assert.notBlank(extension, "Parameter \"file\" " +
                "has no extension and parameter \"extension\" also is blank. ");
        T fileEntity = FileFactory.getInstance(extension);
        if (StringUtils.isNotBlank(charset)
                && fileEntity instanceof TextFile) {
            ((TextFile) fileEntity).setCharset(charset);
        }
        try {
            fileEntity.readFromFile(file);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        return fileEntity;
    }

}
