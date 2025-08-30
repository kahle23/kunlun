/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.common.constant.Charsets;
import kunlun.exception.ExceptionUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ArrayUtil;
import kunlun.util.Assert;
import kunlun.util.ThreadUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;

import static kunlun.common.constant.Numbers.*;

/**
 * The file tools.
 * @author Kahle
 */
public class FileUtil {
    private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 10;
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static boolean rename(File path, String newName) {
        File dest = new File(path.getParent(), newName);
        return path.renameTo(dest);
    }

    public static boolean deleteFile(File destination) {

        return destination == null || !destination.exists() || destination.delete();
    }

    public static void deleteDirectory(File destination) {
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(destination.isDirectory(), "Parameter \"destination\" must be a directory. ");
        // Stack model.
        LinkedList<File> fileList = new LinkedList<File>();
        fileList.addFirst(destination);
        while (!fileList.isEmpty()) {
            File current = fileList.removeFirst();
            File[] files = current.listFiles();
            // Don't have sub file or subdirectory, try to delete.
            if (ArrayUtil.isEmpty(files)) {
                if (!current.delete()) {
                    log.info("Directory \"{}\" delete fail. ", current);
                }
                continue;
            }
            // Is add current?
            boolean addCurrent = false;
            boolean isDirectory;
            for (File file : files) {
                isDirectory = file.isDirectory();
                if (isDirectory && !addCurrent) {
                    fileList.addLast(current);
                    addCurrent = true;
                }
                if (isDirectory) {
                    fileList.addFirst(file);
                }
                else if (!file.delete()) {
                    log.info("File \"{}\" delete fail. ", file);
                }
            }
            if (!addCurrent && !current.delete()) {
                log.info("Directory \"{}\" delete fail. ", current);
            }
        }
    }

    public static void moveFile(File source, File destination) {
        // To append is false.
        // Meaning the destination directory not exists the source file name's file.
        FileUtil.copyFileToDirectory(source, destination, false);
        FileUtil.deleteFile(source);
    }

    public static void moveDirectory(File source, File destination) {
        FileUtil.copyDirectoryToDirectory(source, destination);
        FileUtil.deleteDirectory(source);
    }

    public static void copyFileToFile(File source, File destination, boolean append) {
        // Copy file to file the meaning the destination must be a file.
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(source.exists(), "Parameter \"source\" must exists. ");
        Assert.state(source.isFile(), "Parameter \"source\" must be a file. ");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            if (!destination.exists()) {
                File parentFile = destination.getParentFile();
                boolean noParent = parentFile != null && !parentFile.exists();
                if (noParent && !parentFile.mkdirs()) {
                    throw new IllegalStateException("Create destination parent directory \"" + parentFile + "\" fail. ");
                }
                if (!destination.createNewFile()) {
                    throw new IllegalStateException("Create destination file \"" + destination + "\" fail. ");
                }
            }
            fis = new FileInputStream(source);
            fos = new FileOutputStream(destination, append);
            input  = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = ZERO;
            long count;
            while (pos < size) {
                count = size - pos;
                count = Math.min(count, FILE_COPY_BUFFER_SIZE);
                pos += output.transferFrom(input, pos, count);
            }
        } catch (IOException e) {
            throw ExceptionUtil.wrap(e);
        } finally {
            IoUtil.closeQuietly(output, fos, input, fis);
        }
        if (source.length() != destination.length()) {
            throw new IllegalStateException("Failed to copy full contents from \"" + source + "\" to \"" + destination + "\". ");
        }
    }

    public static void copyFileToDirectory(File source, File destination, boolean append) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        String sourceFileName = source.getName();
        Assert.notBlank(sourceFileName, "Source file name must not blank. ");
        File destFile = new File(destination, sourceFileName);
        FileUtil.copyFileToFile(source, destFile, append);
    }

    public static void copyDirectoryToDirectory(File source, File destination) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(source.exists(), "Parameter \"source\" must exists. ");
        Assert.state(source.isDirectory(), "Parameter \"source\" must be a directory. ");
        if (!destination.exists() && !destination.mkdirs()) {
            throw new IllegalStateException("Create destination directory \"" + destination + "\" fail. ");
        }
        LinkedList<File> fileList = new LinkedList<File>();
        fileList.add(source);
        while (!fileList.isEmpty()) {
            File[] files = fileList.removeFirst().listFiles();
            if (ArrayUtil.isEmpty(files)) { continue; }
            for (File file : files) {
                String subPath = FilenameUtil.subPath(file, source);
                File destPath = new File(destination, subPath);
                if (file.isDirectory()) {
                    if (!destPath.exists() && !destPath.mkdirs()) {
                        throw new IllegalStateException("Create directory \"" + destPath + "\" fail. ");
                    }
                    fileList.addFirst(file);
                }
                else {
                    // To append is false meaning the destination directory is new.
                    FileUtil.copyFileToFile(file, destPath, false);
                }
            }
        }
    }


    // region ======== read ========

    public static String readUtf8String(String path) {
        if (path == null) { return null; }
        return readString(path, Charsets.UTF_8);
    }

    public static String readUtf8String(File file) {
        if (file == null) { return null; }
        return readString(file, Charsets.UTF_8);
    }

    public static String readString(String path, Charset charset) {
        if (path == null) { return null; }
        return readString(new File(path), charset);
    }

    public static String readString(File file, Charset charset) {
        if (file == null) { return null; }
        return new String(readBytes(file), charset);
    }

    public static byte[] readBytes(String path) {
        if (path == null) { return null; }
        return readBytes(new File(path));
    }

    public static byte[] readBytes(File file) {
        if (file == null) { return null; }
        return IoUtil.readBytes(IoUtil.toStream(file));
    }

    // endregion ======== read ========


    // region ======== write ========

    public static File appendUtf8String(String content, String path) {

        return appendString(content, path, Charsets.UTF_8);
    }

    public static File appendUtf8String(String content, File file) {

        return appendString(content, file, Charsets.UTF_8);
    }

    public static File appendString(String content, String path, Charset charset) {

        return appendString(content, createNewFile(path), charset);
    }

    public static File appendString(String content, File file, Charset charset) {

        return writeString(content, file, charset, true);
    }

    public static File writeUtf8String(String content, String path) {

        return writeString(content, path, Charsets.UTF_8);
    }

    public static File writeUtf8String(String content, File file) {

        return writeString(content, file, Charsets.UTF_8);
    }

    public static File writeString(String content, String path, Charset charset) {

        return writeString(content, createNewFile(path), charset);
    }

    public static File writeString(String content, File file, Charset charset) {

        return writeString(content, file, charset, false);
    }

    public static File writeString(String content, File file, Charset charset, boolean isAppend) {
        BufferedWriter writer = null;
        try {
            writer = getWriter(file, charset, isAppend);
            writer.write(content); writer.flush();
            return file;
        } catch (IOException e) {
            throw ExceptionUtil.wrap(e);
        } finally {
            IoUtil.closeQuietly(writer);
        }
    }

    public static File writeBytes(byte[] data, String path) {

        return writeBytes(data, new File(path));
    }

    public static File writeBytes(byte[] data, File file) {

        return writeBytes(data, file, ZERO, data.length, false);
    }

    public static File writeBytes(byte[] data, File file, int off, int len, boolean isAppend) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(createNewFile(file), isAppend);
            out.write(data, off, len); out.flush();
            return file;
        } catch (IOException e) {
            throw ExceptionUtil.wrap(e);
        } finally {
            IoUtil.closeQuietly(out);
        }
    }

    public static File writeFromStream(InputStream in, File file) {

        return writeFromStream(in, file, true);
    }

    public static File writeFromStream(InputStream in, File file, boolean isCloseIn) {
        OutputStream out = null;
        try {
            IoUtil.copy(in, out = FileUtil.getOutputStream(file));
            return file;
        } finally {
            IoUtil.closeQuietly(out);
            if (isCloseIn) { IoUtil.closeQuietly(in); }
        }
    }

    // endregion ======== write ========


    // region ======== get input / reader ========

    public static BufferedReader getUtf8Reader(String path) {

        return getReader(path, Charsets.UTF_8);
    }

    public static BufferedReader getUtf8Reader(File file) {

        return getReader(file, Charsets.UTF_8);
    }

    public static BufferedReader getReader(String path, Charset charset) {

        return getReader(new File(path), charset);
    }

    public static BufferedReader getReader(File file, Charset charset) {

        return IoUtil.toReader(getInputStream(file), charset);
    }

    public static BufferedInputStream getInputStream(String path) {

        return getInputStream(new File(path));
    }

    public static BufferedInputStream getInputStream(File file) {

        return IoUtil.toBuffered(IoUtil.toStream(file));
    }

    // endregion ======== get input / reader ========


    // region ======== get output / writer ========

    public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) {

        return getWriter(new File(path), charset, isAppend);
    }

    public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) {
        try {
            OutputStream out = new FileOutputStream(createNewFile(file), isAppend);
            return new BufferedWriter(new OutputStreamWriter(out, charset));
        } catch (IOException e) { throw ExceptionUtil.wrap(e); }
    }

    public static BufferedOutputStream getOutputStream(String path) {

        return getOutputStream(new File(path));
    }

    public static BufferedOutputStream getOutputStream(File file) {
        try {
            OutputStream in = new FileOutputStream(createNewFile(file));
            return IoUtil.toBuffered(in);
        } catch (IOException e) { throw ExceptionUtil.wrap(e); }
    }

    // endregion ======== get output / writer ========


    // region ======== create new file ========

    public static File createNewFile(String parent, String child) {

        return createNewFile(new File(parent, child));
    }

    public static File createNewFile(File parent, String child) {

        return createNewFile(new File(parent, child));
    }

    public static File createNewFile(String path) {
        if (path == null) { return null; }
        return createNewFile(new File(path));
    }

    public static File createNewFile(File file) {
        if (file == null) { return null; }
        if (file.exists()) { return file; }
        mkdirs(file.getParentFile());
        try {
            if (!file.createNewFile()) {
                throw new IllegalStateException("Create file \"" + file + "\" fail. ");
            }
            return file;
        } catch (IOException e) { throw ExceptionUtil.wrap(e); }
    }

    // endregion ======== create new file ========


    // region ======== mkdirs ========

    public static File mkdirs(String dirPath) {
        if (dirPath == null) { return null; }
        return mkdirs(new File(dirPath));
    }

    public static File mkdirs(File dir) {
        if (dir == null) { return null; }
        if (!dir.exists()) {
            if (!mkdirsSafely(dir, FIVE, ONE)) {
                throw new IllegalStateException("Create directory \"" + dir + "\" fail. ");
            }
        }
        return dir;
    }

    public static boolean mkdirsSafely(File dir, int tryCount, long sleepMillis) {
        if (dir == null) { return false; }
        if (dir.isDirectory()) { return true; }
        for (int i = ONE; i <= tryCount; i++) {
            // In high concurrency scenarios, If the file already exists, it will also return false.
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            if (dir.exists()) { return true; }
            ThreadUtil.sleepQuietly(sleepMillis);
        }
        return dir.exists();
    }

    // endregion ======== mkdirs ========

}
