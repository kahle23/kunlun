package artoria.file;

import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.CloseUtils;

import java.io.*;

import static artoria.common.Constants.CLASSPATH;

/**
 * Abstract file entity.
 * @author Kahle
 */
public abstract class AbstractFileEntity implements FileEntity {
    private String fileName;
    private String extension;

    @Override
    public String getName() {

        return fileName;
    }

    @Override
    public void setName(String fileName) {
        Assert.notBlank(fileName
                , "Parameter \"fileName\" must not blank. ");
        this.fileName = fileName;
    }

    @Override
    public String getExtension() {

        return extension;
    }

    @Override
    public void setExtension(String extension) {
        extension = extension == null
                ? null : extension.trim().toLowerCase();
        this.extension = extension;
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
            CloseUtils.closeQuietly(outputStream);
        }
    }

    public long readFromFile(File file) throws IOException {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        Assert.state(file.exists() && file.isFile()
                , "Parameter \"file\" must exist and is file. ");
        String fileName = file.getName();
        String fileString = file.toString();
        String extension = FilenameUtils.getExtension(fileString);
        this.setName(fileName);
        this.setExtension(extension);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return this.read(inputStream);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    public void writeToClasspath(String subPath) throws IOException {
        Assert.notBlank(subPath, "Parameter \"subPath\" must not blank. ");
        Assert.notBlank(CLASSPATH, "Cannot get the classpath. ");
        this.writeToFile(new File(CLASSPATH, subPath));
    }

    public long readFromClasspath(String subPath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = ClassLoaderUtils
                    .getResourceAsStream(subPath, this.getClass());
            Assert.notNull(inputStream
                    , "Parameter \"subPath\" not found in classpath. ");
            this.setName(new File(subPath).getName());
            this.setExtension(FilenameUtils.getExtension(subPath));
            return this.read(inputStream);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

}
