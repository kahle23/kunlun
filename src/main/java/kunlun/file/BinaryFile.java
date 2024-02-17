/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.file;

import kunlun.util.Assert;

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
        write(outputStream);
        return outputStream.toByteArray();
    }

    public long readFromByteArray(byte[] byteArray) throws IOException {
        Assert.notEmpty(byteArray, "Parameter \"byteArray\" must not empty. ");
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        return read(inputStream);
    }

}
