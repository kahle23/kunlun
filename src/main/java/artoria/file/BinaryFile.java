package artoria.file;

import artoria.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract binary file.
 * @author Kahle
 */
public abstract class BinaryFile extends AbstractFileEntity {

    public byte[] writeToByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.write(outputStream);
        return outputStream.toByteArray();
    }

    public long readFromByteArray(byte[] byteArray) throws IOException {
        Assert.notEmpty(byteArray, "Parameter \"byteArray\" must not empty. ");
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        return this.read(inputStream);
    }

}
