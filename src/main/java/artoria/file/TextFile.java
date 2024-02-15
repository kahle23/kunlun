package artoria.file;

import artoria.util.Assert;

import java.io.*;

import static artoria.common.constant.Charsets.STR_DEFAULT_CHARSET;

/**
 * Abstract text file.
 * @author Kahle
 */
public abstract class TextFile extends BinaryFile {
    private String charset = STR_DEFAULT_CHARSET;

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String writeToString() throws IOException {
        byte[] byteArray = writeToByteArray();
        return new String(byteArray, getCharset());
    }

    public long readFromString(String text) throws IOException {
        Assert.notBlank(text, "Parameter \"text\" must not blank. ");
        byte[] byteArray = text.getBytes(getCharset());
        return readFromByteArray(byteArray);
    }

    @Override
    public long read(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        Reader isr = new InputStreamReader(inputStream, getCharset());
        Reader reader = new BufferedReader(isr);
        return read(reader);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        Assert.notNull(outputStream, "Parameter \"outputStream\" must not null. ");
        Writer osw = new OutputStreamWriter(outputStream, getCharset());
        Writer writer = new BufferedWriter(osw);
        write(writer);
        writer.flush();
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
