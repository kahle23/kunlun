package saber;

import java.io.*;
import java.nio.charset.Charset;

public class IOUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

    public static InputStream findClasspath(String fileName) {
        return ClassUtils.getDefaultClassLoader().getResourceAsStream(fileName);
    }

    public static InputStream toInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static Reader toReader(String data) {
        return new StringReader(data);
    }

    public static byte[] toByteArray(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyLarge(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(output, encoding);
        copyLarge(input, writer);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input)
            throws IOException {
        return toByteArray(input, DEFAULT_CHARSET_NAME);
    }

    public static char[] toCharArray(InputStream input, String encoding)
            throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copyLarge(new InputStreamReader(input, encoding), output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input)
            throws IOException {
        return toCharArray(input, DEFAULT_CHARSET_NAME);
    }

    public static char[] toCharArray(Reader input)
            throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        copyLarge(input, writer);
        return writer.toCharArray();
    }

    public static String toString(InputStream input, String encoding)
            throws IOException {
        StringWriter writer = new StringWriter();
        copyLarge(new InputStreamReader(input, encoding), writer);
        return writer.toString();
    }

    public static String toString(InputStream input)
            throws IOException {
        return toString(input, DEFAULT_CHARSET_NAME);
    }

    public static String toString(Reader input)
            throws IOException {
        StringWriter writer = new StringWriter();
        copyLarge(input, writer);
        return writer.toString();
    }

    public static long copyLarge(Reader input, Writer output)
            throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(Reader input, Writer output, char [] buffer)
            throws IOException {
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

}
