package artoria.io.file.support;

import artoria.data.KeyValue;
import artoria.data.Pair;
import artoria.exception.ExceptionUtils;
import artoria.io.FileBase;
import artoria.io.FileEntity;
import artoria.io.storage.AbstractDataStorage;
import artoria.io.util.FileUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static artoria.common.Constants.SLASH;
import static artoria.common.Constants.UTF_8;

/**
 * The local file storage.
 * @author Kahle
 */
public class LocalFileStorage extends AbstractDataStorage {
    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
    private final String charset;

    public LocalFileStorage(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public LocalFileStorage() {

        this(UTF_8);
    }

    protected File convertToFile(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (key instanceof String) {
            Assert.notBlank((String) key, "Parameter \"key\" must not blank. ");
            return new File((String) key);
        }
        else if (key instanceof File) {
            return (File) key;
        }
        else if (key instanceof FileBase) {
            String path = ((FileBase) key).getPath();
            Assert.notBlank(path, "Parameter \"path\" must not null. ");
            return new File(path);
        }
        else {
            throw new IllegalArgumentException("Parameter \"key\" is not supported. ");
        }
    }

    @Override
    public boolean exist(Object key) {

        return convertToFile(key).exists();
    }

    @Override
    public FileEntity get(Object key) {
        File file = convertToFile(key);
        if (!file.exists()) { return null; }
        Assert.isTrue(file.isFile(), "Parameter \"key\" must correspond to a file. ");
        String name = file.getName();
        String path = file.getPath();
        InputStream inputStream;
        try { inputStream = new FileInputStream(file); }
        catch (FileNotFoundException e) {
            throw ExceptionUtils.wrap(e);
        }
        return new FileEntityImpl(name, path, inputStream);
    }

    @Override
    public Object put(Object data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        InputStream inputStream = null;
        String path;
        try {
            if (data instanceof FileEntity) {
                FileEntity fileEntity = (FileEntity) data;
                inputStream = fileEntity.getInputStream();
                path = fileEntity.getPath();
            }
            else if (data instanceof KeyValue) {
                @SuppressWarnings("rawtypes")
                KeyValue keyValue = (KeyValue) data;
                inputStream = convertToStream(keyValue.getValue(), charset);
                path = keyValue.getKey() != null ? String.valueOf(keyValue.getKey()) : null;
            }
            else if (data instanceof Pair) {
                @SuppressWarnings("rawtypes")
                Pair pair = (Pair) data;
                inputStream = convertToStream(pair.getRight(), charset);
                path = pair.getLeft() != null ? String.valueOf(pair.getLeft()) : null;
            }
            else {
                throw new IllegalArgumentException("Parameter \"data\" is not supported. ");
            }
            Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
            Assert.notNull(path, "Parameter \"path\" must not null. ");
            return FileUtils.write(inputStream, new File(path));
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public Boolean delete(Object key) {
        File file = convertToFile(key);
        return file.exists() && file.delete();
    }

    @Override
    public Collection<FileBase> list(Object conditions) {
        Assert.notNull(conditions, "Parameter \"conditions\" must not null. ");
        if (ObjectUtils.isEmpty(conditions)) { conditions = SLASH; }
        File file = new File(String.valueOf(conditions));
        File[] files = file.listFiles();
        if (files == null) { return null; }
        List<FileBase> list = new ArrayList<FileBase>();
        for (File filePath : files) {
            if (filePath == null) { continue; }
            list.add(new FileBaseImpl(filePath.getName(), filePath.getPath()));
        }
        return list;
    }

}
