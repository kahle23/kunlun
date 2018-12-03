package artoria.io;

import artoria.reflect.ReflectUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.Selector;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;
import static artoria.common.Constants.SLASH;

/**
 * IO tools.
 * @author Kahle
 */
public class IOUtils {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int EOF = -1;

    public static InputStream findClasspath(String fileName) {
        if (!fileName.startsWith(SLASH)) {
            fileName = SLASH + fileName;
        }
        return IOUtils.class.getResourceAsStream(fileName);
    }

//    TODO: 1.7
//    public static void closeQuietly(AutoCloseable closeable) {
//        if (closeable != null) {
//            try {
//                closeable.close();
//            }
//            catch (Exception e) {
//                // ignore
//            }
//        }
//    }
//    TODO: 1.7 Remove when jdk 1.7
    public static void closeQuietly(Object closeable) {
        if (closeable != null) {
            try {
                String close = "close";
                Class<?> clazz = closeable.getClass();
                Method method = ReflectUtils.findMethod(clazz, close);
                method.invoke(closeable);
            }
            catch (Exception e) {
                // ignore
            }
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

    public static void closeQuietly(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
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

    public static byte[] toByteArray(Reader input) throws IOException {

        return IOUtils.toByteArray(input, DEFAULT_CHARSET_NAME);
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

        return IOUtils.toString(input, DEFAULT_CHARSET_NAME);
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
