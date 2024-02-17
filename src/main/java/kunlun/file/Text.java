/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.file;

import kunlun.io.util.IOUtils;
import kunlun.util.Assert;

import java.io.*;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.EMPTY_STRING;

/**
 * Text file.
 * @author Kahle
 */
public class Text extends TextFile {
    private final StringBuilder textBuilder = new StringBuilder();

    @Override
    public InputStream getInputStream() throws IOException {
        String toString = textBuilder.toString();
        String charset = getCharset();
        byte[] stringBytes = toString.getBytes(charset);
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long read(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String read = IOUtils.toString(reader);
        textBuilder.setLength(ZERO);
        textBuilder.append(read);
        return read.length();
    }

    @Override
    public void write(Writer writer) throws IOException {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        writer.write(textBuilder.toString());
    }

    public Text append(Object obj) {
        textBuilder.append(obj != null ? obj : EMPTY_STRING);
        return this;
    }

    public Text append(Object obj, int start, int end) {
        String input = obj != null ? obj.toString() : EMPTY_STRING;
        textBuilder.append(input, start, end);
        return this;
    }

    public Text clear() {
        textBuilder.setLength(ZERO);
        return this;
    }

    @Override
    public String toString() {

        return textBuilder.toString();
    }

}
