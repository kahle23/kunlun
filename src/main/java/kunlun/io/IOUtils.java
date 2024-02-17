/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io;

import kunlun.io.util.StringBuilderWriter;

import java.io.*;

import static kunlun.common.constant.Charsets.STR_DEFAULT_CHARSET;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * The io tools.
 * @author Kahle
 */
@Deprecated
public class IOUtils {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int EOF = -1;

    public static byte[] toByteArray(Reader input) throws IOException {

        return IOUtils.toByteArray(input, STR_DEFAULT_CHARSET);
    }

    public static byte[] toByteArray(Reader input, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer output = new OutputStreamWriter(out, charset);
        IOUtils.copyLarge(input, output);
        return out.toByteArray();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copyLarge(input, output);
        return output.toByteArray();
    }

    public static String toString(InputStream input) throws IOException {

        return IOUtils.toString(input, STR_DEFAULT_CHARSET);
    }

    public static String toString(InputStream input, String charset) throws IOException {
        StringBuilderWriter output = new StringBuilderWriter();
        Reader reader = new InputStreamReader(input, charset);
        copyLarge(reader, output);
        return output.toString();
    }

    public static String toString(Reader input) throws IOException {
        StringBuilderWriter output = new StringBuilderWriter();
        copyLarge(input, output);
        return output.toString();
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buf = new char[DEFAULT_BUFFER_SIZE];
        return IOUtils.copyLarge(input, output, buf);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = ZERO;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, ZERO, n);
        }
        output.flush();
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        return IOUtils.copyLarge(input, output, buf);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = ZERO;
        for (int len; EOF != (len = input.read(buffer)); count += len) {
            output.write(buffer, ZERO, len);
        }
        output.flush();
        return count;
    }

}
