package artoria.io.file.support;

import artoria.io.FileBase;
import artoria.util.Assert;

/**
 * The file basic information implementation class.
 * @author Kahle
 */
public class FileBaseImpl implements FileBase {
    private String charset;
    private String path;
    private String name;

    public FileBaseImpl(String name, String path) {
        Assert.notNull(path, "Parameter \"path\" must not null. ");
        this.path = path;
        this.name = name;
    }

    public FileBaseImpl(String path) {

        this(null, path);
    }

    public FileBaseImpl() {

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

}
