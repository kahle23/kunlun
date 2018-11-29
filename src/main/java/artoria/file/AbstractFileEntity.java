package artoria.file;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.*;

/**
 * Abstract file entity.
 * @author Kahle
 */
public abstract class AbstractFileEntity implements FileEntity {
    private String extension;

    public AbstractFileEntity(String extension) {
        Assert.notBlank(extension, "Parameter \"extension\" must not blank. ");
        this.extension = extension.toLowerCase().trim();
    }

    @Override
    public String getExtension() {

        return extension;
    }

    public void writeToFile(File file) throws IOException {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        File parentFile = file.getParentFile();
        Assert.state(parentFile.exists() || parentFile.mkdirs()
                , "The parent directory for the parameter \"file\" does not exist and creation failed. ");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            this.write(outputStream);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public long readFromFile(File file) throws IOException {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        Assert.state(file.exists() && file.isFile()
                , "Parameter \"file\" must exist and is file. ");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return this.read(inputStream);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
