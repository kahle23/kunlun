package artoria.io.file.support;

import artoria.io.file.FileBase;
import artoria.util.Assert;

/**
 * The file basic information implementation class.
 * @author Kahle
 */
public class FileBaseImpl implements FileBase {
    private final String path;
    private final String name;

    public FileBaseImpl(String path) {

        this(null, path);
    }

    public FileBaseImpl(String name, String path) {
        Assert.notNull(path, "Parameter \"path\" must not null. ");
        this.path = path;
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getPath() {

        return path;
    }

}
