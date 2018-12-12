package artoria.file;

import artoria.util.Assert;

import java.io.*;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

/**
 * Abstract text file.
 * @author Kahle
 */
public abstract class TextFile extends BinaryFile {
    private String charset = DEFAULT_CHARSET_NAME;

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String writeToString() throws IOException {
        byte[] byteArray = this.writeToByteArray();
        return new String(byteArray, this.getCharset());
    }

    public long readFromString(String text) throws IOException {
        Assert.notBlank(text, "Parameter \"text\" must not blank. ");
        byte[] byteArray = text.getBytes(this.getCharset());
        return this.readFromByteArray(byteArray);
    }

    @Override
    public long read(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        Reader reader = new InputStreamReader(inputStream, this.getCharset());
        return this.read(reader);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        Assert.notNull(outputStream, "Parameter \"outputStream\" must not null. ");
        Writer writer = new OutputStreamWriter(outputStream, this.getCharset());
        this.write(writer);
    }

    /**
     * Read from character input stream.
     * @param reader The character input stream that will be read
     * @return Read length
     * @throws IOException Maybe it will come up
     */
    public abstract long read(Reader reader) throws IOException;

    /**
     * Write to character output stream.
     * @param writer The character output stream that will be write
     * @throws IOException Maybe it will come up
     */
    public abstract void write(Writer writer) throws IOException;

}
