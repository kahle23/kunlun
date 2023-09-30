package artoria.io.file.support;

import artoria.io.FileEntity;
import artoria.util.Assert;

import java.io.InputStream;

/**
 * The file entity object implementation class.
 * @author Kahle
 */
public class FileEntityImpl implements FileEntity {
    private InputStream inputStream;
    private String charset;
    private String path;
    private String name;

    public FileEntityImpl(String name, String path, InputStream inputStream) {
        Assert.notNull(path, "Parameter \"path\" must not null. ");
        this.inputStream = inputStream;
        this.path = path;
        this.name = name;
    }

    public FileEntityImpl(String name, String path) {

        this(name, path, null);
    }

    public FileEntityImpl(String path) {

        this(null, path, null);
    }

    public FileEntityImpl() {

    }

    @Override
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @Override
    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    @Override
    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {

        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() {

        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {

        this.inputStream = inputStream;
    }

}
