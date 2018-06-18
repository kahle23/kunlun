package artoria.io;

import artoria.util.Const;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;

import static artoria.util.Const.SLASH;

/**
 * IO tools.
 * @author Kahle
 */
public class IOUtils {
    private static final Class<?> THIS_CLASS = IOUtils.class;
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int EOF = -1;

    public static InputStream findClasspath(String fileName) {
        if (!fileName.startsWith(SLASH)) {
            fileName = SLASH + fileName;
        }
        return THIS_CLASS.getResourceAsStream(fileName);
    }

    public static void closeQuietly(URLConnection conn) {
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

    public static Reader toReader(byte[] data) {
        return IOUtils.toReader(data, Const.DEFAULT_CHARSET_NAME);
    }

    public static Reader toReader(byte[] data, String charset) {
        Charset encoding = Charset.forName(charset);
        return new StringReader(new String(data, encoding));
    }

    public static Reader toReader(String data) {
        return new StringReader(data);
    }

    public static InputStream toInputStream(String data) {
        return IOUtils.toInputStream(data, Const.DEFAULT_CHARSET_NAME);
    }

    public static InputStream toInputStream(String data, String charset) {
        Charset encoding = Charset.forName(charset);
        byte[] bytes = data.getBytes(encoding);
        return new ByteArrayInputStream(bytes);
    }

    public static InputStream toInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        return IOUtils.toByteArray(input, Const.DEFAULT_CHARSET_NAME);
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
        return IOUtils.toString(input, Const.DEFAULT_CHARSET_NAME);
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
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        return IOUtils.copyLarge(input, output, buf);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        for (int n; EOF != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        output.flush();
        return count;
    }

}
