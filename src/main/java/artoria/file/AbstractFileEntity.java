package artoria.file;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.*;

import static artoria.common.Constants.CLASSPATH;

/**
 * Abstract file entity.
 * @author Kahle
 */
public abstract class AbstractFileEntity implements FileEntity {

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

    public void writeToClasspath(String subpath) throws IOException {
        Assert.notBlank(subpath, "Parameter \"subpath\" must not blank. ");
        Assert.notBlank(CLASSPATH, "Cannot get the classpath. ");
        this.writeToFile(new File(CLASSPATH, subpath));
    }

    public long readFromClasspath(String subpath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.findClasspath(subpath);
            Assert.notNull(inputStream
                    , "Parameter \"subpath\" not found in classpath. ");
            return this.read(inputStream);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
