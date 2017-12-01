package artoria.util;

import org.apache.commons.io.output.StringBuilderWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;

/**
 * @author Kahle
 */
public class IOUtils {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int EOF = -1;

    public static void close(URLConnection conn) {
        if (conn != null && conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ioe) {
                // ignore
            }
        }
    }

    public static void closeQuietly(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            }
            catch (IOException ioe) {
                // ignored
            }
        }
    }

    public static InputStream findClasspath(String fileName) {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public static InputStream toInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static Reader toReader(String data) {
        return new StringReader(data);
    }

    public static byte[] toByteArray(Reader input)
            throws IOException {
        return IOUtils.toByteArray(input, DEFAULT_CHARSET_NAME);
    }

    public static byte[] toByteArray(Reader input
            , String encoding) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer output = new OutputStreamWriter(out, encoding);
        IOUtils.copyLarge(input, output);
        return out.toByteArray();
    }

    public static byte[] toByteArray(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copyLarge(input, output);
        return output.toByteArray();
    }

    public static char[] toCharArray(InputStream input)
            throws IOException {
        return IOUtils.toCharArray(input, DEFAULT_CHARSET_NAME);
    }

    public static char[] toCharArray(InputStream input
            , String encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        Reader reader = new InputStreamReader(input, encoding);
        IOUtils.copyLarge(reader, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input)
            throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        IOUtils.copyLarge(input, output);
        return output.toCharArray();
    }

    public static String toString(InputStream input)
            throws IOException {
        return IOUtils.toString(input, DEFAULT_CHARSET_NAME);
    }

    public static String toString(InputStream input
            , String encoding) throws IOException {
        StringBuilderWriter output = new StringBuilderWriter();
        Reader reader = new InputStreamReader(input, encoding);
        IOUtils.copyLarge(reader, output);
        return output.toString();
    }

    public static String toString(Reader input)
            throws IOException {
        StringBuilderWriter output = new StringBuilderWriter();
        IOUtils.copyLarge(input, output);
        return output.toString();
    }

    public static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buf = new char[DEFAULT_BUFFER_SIZE];
        return IOUtils.copyLarge(input, output, buf);
    }

    public static long copyLarge(Reader input, Writer output
            , char[] buffer) throws IOException {
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        return IOUtils.copyLarge(input, output, buf);
    }

    public static long copyLarge(InputStream input, OutputStream output
            , byte[] buffer) throws IOException {
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

}
