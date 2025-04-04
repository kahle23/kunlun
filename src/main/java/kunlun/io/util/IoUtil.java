/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.common.constant.Charsets;
import kunlun.exception.ExceptionUtil;
import kunlun.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * The io tools.
 * @author Kahle
 */
public class IoUtil {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int EOF = -1;


    // region ======== to reader / writer ========

    public static BufferedReader toUtf8Reader(InputStream in) {

        return toReader(in, Charsets.UTF_8);
    }

    public static BufferedReader toReader(InputStream in, Charset charset) {
        if (in == null) { return null; }
        InputStreamReader reader;
        if (charset == null) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }
        return new BufferedReader(reader);
    }

    public static BufferedWriter toUtf8Writer(OutputStream out) {

        return toWriter(out, Charsets.UTF_8);
    }

    public static BufferedWriter toWriter(OutputStream out, Charset charset) {
        if (out == null) { return null; }
        OutputStreamWriter writer;
        if (charset == null) {
            writer = new OutputStreamWriter(out);
        } else {
            writer = new OutputStreamWriter(out, charset);
        }
        return new BufferedWriter(writer);
    }

    // endregion ======== to reader / writer ========


    // region ======== to stream / buffered ========

    public static BufferedWriter toBuffered(Writer writer) {
        Assert.notNull(writer);
        return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedWriter toBuffered(Writer writer, int bufferSize) {
        Assert.notNull(writer);
        return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
    }

    public static BufferedReader toBuffered(Reader reader) {
        Assert.notNull(reader);
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBuffered(Reader reader, int bufferSize) {
        Assert.notNull(reader);
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
    }

    public static BufferedOutputStream toBuffered(OutputStream out) {
        Assert.notNull(out);
        return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
    }

    public static BufferedOutputStream toBuffered(OutputStream out, int bufferSize) {
        Assert.notNull(out);
        return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
    }

    public static BufferedInputStream toBuffered(InputStream in) {
        Assert.notNull(in);
        return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public static BufferedInputStream toBuffered(InputStream in, int bufferSize) {
        Assert.notNull(in);
        return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
    }

    public static InputStream toStream(File file) {
        Assert.notNull(file);
        try {
            return new FileInputStream(file);
        } catch (IOException ioe) {
            throw ExceptionUtil.wrap(ioe);
        }
    }

    public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
        if (out == null) { return null; }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream toUtf8Stream(String content) {

        return toStream(content, Charsets.UTF_8);
    }

    public static ByteArrayInputStream toStream(String content, Charset charset) {
        if (content == null) { return null; }
        return toStream(charset != null ? content.getBytes(charset) : content.getBytes());
    }

    public static ByteArrayInputStream toStream(byte[] content) {
        if (content == null) { return null; }
        return new ByteArrayInputStream(content);
    }

    // endregion ======== to stream / buffered ========


    // region ======== write ========

    public static void writeUtf8(OutputStream out, boolean isCloseOut, CharSequence... contents) {

        writeStr(out, Charsets.UTF_8, isCloseOut, contents);
    }

    public static void writeStr(OutputStream out, Charset charset, boolean isCloseOut, CharSequence... contents) {
        try {
            BufferedWriter writer = IoUtil.toWriter(out, charset);
            for (CharSequence content : contents) {
                if (content != null) {
                    writer.write(content.toString());
                }
            }
            writer.flush();
        } catch (IOException ioe) {
            throw ExceptionUtil.wrap(ioe);
        } finally {
            if (isCloseOut) { IoUtil.closeQuietly(out); }
        }
    }

    public static void writeClose(OutputStream out, byte[] content) {

        write(out, true, content);
    }

    public static void write(OutputStream out, byte[] content) {

        write(out, false, content);
    }

    public static void write(OutputStream out, boolean isCloseOut, byte[] content) {
        try {
            out.write(content);
        } catch (IOException ioe) {
            throw ExceptionUtil.wrap(ioe);
        } finally {
            if (isCloseOut) { IoUtil.closeQuietly(out); }
        }
    }

    // endregion ======== write ========


    // region ======== read ========

    public static String readUtf8(InputStream in) {

        return read(in, Charsets.UTF_8);
    }

    public static String read(InputStream in, Charset charset) {

        return new String(readBytes(in), charset);
    }

    public static String read(Reader reader) {

        return read(reader, true);
    }

    public static String read(Reader reader, boolean isClose) {
        StringBuilderWriter writer = new StringBuilderWriter();
        try {
            copy(reader, writer); return writer.toString();
        } finally { if (isClose) { closeQuietly(reader); } }
    }

    public static byte[] readBytes(InputStream in) {

        return readBytes(in, true);
    }

    public static byte[] readBytes(InputStream in, boolean isClose) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            copy(in, out); return out.toByteArray();
        } finally { if (isClose) { closeQuietly(in); } }
    }

    // endregion ======== read ========


    // region ======== copy ========

    public static long copy(Reader reader, Writer writer) {

        return IoUtil.copy(reader, writer, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(Reader reader, Writer writer, char[] buffer) {
        try {
            return doCopy(reader, writer, buffer);
        } catch (IOException ioe) {
            throw ExceptionUtil.wrap(ioe);
        }
    }

    public static long doCopy(Reader reader, Writer writer, char[] buffer) throws IOException {
        long count = ZERO;
        for (int len; EOF != (len = reader.read(buffer)); count += len) {
            writer.write(buffer, ZERO, len);
        }
        writer.flush();
        return count;
    }

    public static long copy(InputStream in, OutputStream out) {

        return IoUtil.copy(in, out, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(InputStream in, OutputStream out, byte[] buffer) {
        try {
            return doCopy(in, out, buffer);
        } catch (IOException ioe) {
            throw ExceptionUtil.wrap(ioe);
        }
    }

    public static long doCopy(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        long count = ZERO;
        for (int len; EOF != (len = in.read(buffer)); count += len) {
            out.write(buffer, ZERO, len);
        }
        out.flush();
        return count;
    }

    // endregion ======== copy ========


    // region ======== close ========

    public static void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
    }

//    TODO: 1.7
    /*
    public static void closeQuietly(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
    }*/

    public static void closeIfPossible(Object... objects) {
        for (Object object : objects) {
            if (object == null) { continue; }
            if (object instanceof Closeable) {
                closeQuietly((Closeable) object);
            }
//            if (object instanceof AutoCloseable) {
//                closeQuietly((AutoCloseable) object);
//            }
            else if (object instanceof HttpURLConnection) {
                ((HttpURLConnection) object).disconnect();
            } else if (object instanceof ResultSet) {
                // ---- AutoCloseable ----
                try { ((ResultSet) object).close(); }
                catch (SQLException se) { /* ignore */ }
            } else if (object instanceof Statement) {
                try { ((Statement) object).close(); }
                catch (SQLException se) { /* ignore */ }
            } else if (object instanceof Connection) {
                try { ((Connection) object).close(); }
                catch (SQLException se) { /* ignore */ }
            }
            // ---- AutoCloseable ----
        }
    }

    // endregion ======== close ========

}
