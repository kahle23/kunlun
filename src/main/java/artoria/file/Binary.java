package artoria.file;

import artoria.codec.CodecUtils;
import artoria.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Binary file.
 * @author Kahle
 */
public class Binary extends BinaryFile {
    private byte[] data;

    @Override
    public InputStream getInputStream() throws IOException {

        return new ByteArrayInputStream(data);
    }

    @Override
    public long read(InputStream inputStream) throws IOException {
        data = IOUtils.toByteArray(inputStream);
        return data.length;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {

        outputStream.write(data);
    }

    @Override
    public byte[] writeToByteArray() throws IOException {

        return data;
    }

    public String writeToHexString() {

        return CodecUtils.encodeToString(CodecUtils.HEX, data);
    }

}
