package artoria.io.file.support;

import artoria.exception.ExceptionUtils;
import artoria.io.file.FileEntity;
import artoria.io.file.FilenameUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * The file entity object implementation class.
 * @author Kahle
 */
public class FileEntityImpl implements FileEntity {
    private InputStream inputStream;
    private final String path;
    private final String name;

    public FileEntityImpl(String path) {

        this(null, path, (InputStream) null);
    }

    public FileEntityImpl(String name, String path) {

        this(name, path, (InputStream) null);
    }

    public FileEntityImpl(String path, byte[] bytes) {

        this(null, path, new ByteArrayInputStream(bytes));
    }

    public FileEntityImpl(String path, InputStream inputStream) {

        this(null, path, inputStream);
    }

    public FileEntityImpl(String name, String path, byte[] bytes) {

        this(name, path, new ByteArrayInputStream(bytes));
    }

    public FileEntityImpl(String name, String path, InputStream inputStream) {
        Assert.notNull(path, "Parameter \"path\" must not null. ");
        this.path = path;
        if (inputStream != null) {
            this.inputStream = inputStream;
        }
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        else {
            this.name = FilenameUtils.getName(path);
        }
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getPath() {

        return path;
    }

    @Override
    public InputStream getInputStream() {
        if (inputStream != null) {
            return inputStream;
        }
        if (StringUtils.isBlank(path)) {
            return null;
        }
        try {
            inputStream = new FileInputStream(path);
        }
        catch (FileNotFoundException e) {
            throw ExceptionUtils.wrap(e);
        }
        return inputStream;
    }

}
