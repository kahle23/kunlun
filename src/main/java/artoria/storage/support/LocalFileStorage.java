package artoria.storage.support;

import artoria.exception.ExceptionUtils;
import artoria.io.file.FileUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static artoria.common.Constants.SLASH;
import static artoria.util.ObjectUtils.cast;

/**
 * The local file storage.
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public class LocalFileStorage extends AbstractStreamStorage {
    private static Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
    private final String workDir;

    public LocalFileStorage(String name) {

        this(name, null, System.getProperty("java.io.tmpdir"));
    }

    public LocalFileStorage(String name, String workDir) {

        this(name, null, workDir);
    }

    public LocalFileStorage(String name, String charset, String workDir) {
        super(name, charset);
        Assert.notBlank(workDir, "Parameter \"workDir\" must not blank. ");
        log.info("The working directory of " +
                "the local file storage named \"{}\" is \"{}\". ", name, workDir);
        this.workDir = workDir;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String keyString = getKeyString(key);
        File file = new File(workDir, keyString);
        Assert.isTrue(file.isFile(), "Parameter \"key\" must correspond to a file. ");
        if (!file.exists()) { return null; }
        if (File.class.isAssignableFrom(type)) {
            return cast(file);
        }
        if (InputStream.class.isAssignableFrom(type)) {
            try {
                return cast(new FileInputStream(file));
            }
            catch (FileNotFoundException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return convertToResult(inputStream, getCharset(), type);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        String keyString = getKeyString(key);
        File file = new File(workDir, keyString);
        return file.exists();
    }

    @Override
    public Object put(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        String keyString = getKeyString(key);
        File file = new File(workDir, keyString);
        InputStream inputStream = null;
        try {
            inputStream = convertToStream(value, getCharset());
            return FileUtils.write(inputStream, file);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public Object remove(Object key) {
        String keyString = getKeyString(key);
        File file = new File(workDir, keyString);
        return file.exists() && file.delete();
    }

    @Override
    public <T> Collection<T> keys(Object pattern, Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (ObjectUtils.isEmpty(pattern)) { pattern = SLASH; }
        File file = new File(workDir, String.valueOf(pattern));
        if (CharSequence.class.isAssignableFrom(type)) {
            String[] list = file.list();
            if (list == null) { return null; }
            return ObjectUtils.cast(Arrays.asList(list));
        }
        else if (File.class.isAssignableFrom(type)) {
            File[] files = file.listFiles();
            if (files == null) { return null; }
            return ObjectUtils.cast(Arrays.asList(files));
        }
        else {
            throw new IllegalArgumentException("Parameter \"type\" is not supported. ");
        }
    }

}
