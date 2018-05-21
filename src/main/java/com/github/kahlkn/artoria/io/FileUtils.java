package com.github.kahlkn.artoria.io;

import com.github.kahlkn.artoria.util.ArrayUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.PathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * File tools.
 * @author Kahle
 */
public class FileUtils {
    private static Logger log = Logger.getLogger(FileUtils.class.getName());
    private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 10;

    public static boolean rename(File path, String newName) {
        File dest = new File(path.getParent(), newName);
        return path.renameTo(dest);
    }

    public static byte[] read(File dest) throws IOException {
        Assert.notNull(dest, "Destination must not null. ");
        Assert.state(dest.exists(), "Destination must exists. ");
        Assert.state(dest.isFile(), "Destination must is a file. ");
        FileInputStream in = null;
        try {
            in = new FileInputStream(dest);
            return IOUtils.toByteArray(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static int write(byte[] data, File dest) throws IOException {
        return FileUtils.write(data, dest, false);
    }

    public static int write(byte[] data, File dest, boolean append) throws IOException {
        Assert.notNull(dest, "Destination must not null. ");
        if (!dest.exists() && !dest.createNewFile()) {
            throw new IOException("Create destination file fail. ");
        }
        if (ArrayUtils.isEmpty(data)) { return 0; }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data);
            out.flush();
            return data.length;
        }
        finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static boolean deleteFile(File dest) {
        return dest == null || !dest.exists() || dest.delete();
    }

    public static void deleteDirectory(File dest) {
        Assert.notNull(dest, "Destination must not null. ");
        Assert.state(dest.isDirectory(), "Destination must is a directory. ");
        // stack model
        LinkedList<File> fileList = new LinkedList<File>();
        fileList.addFirst(dest);
        while (!fileList.isEmpty()) {
            File current = fileList.removeFirst();
            File[] files = current.listFiles();
            // don't have subfile or subdirectory, try delete
            if (ArrayUtils.isEmpty(files)) {
                if (!current.delete()) {
                    log.info("File[" + current + "] delete fail. ");
                }
                continue;
            }
            // is add current ?
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
                    log.info("File[" + file + "] delete fail. ");
                }
            }
            if (!addCurrent && !current.delete()) {
                log.info("File[" + current + "] delete fail. ");
            }
        }
    }

    public static void moveFile(File src, File dest) throws IOException {
        // The append is false meaning
        // the destination directory not exists the source file name's file.
        FileUtils.copyFileToDirectory(src, dest, false);
        FileUtils.deleteFile(src);
    }

    public static void moveDirectory(File src, File dest) throws IOException {
        FileUtils.copyDirectoryToDirectory(src, dest);
        FileUtils.deleteDirectory(src);
    }

    public static void copyFileToFile(File src, File dest, boolean append) throws IOException {
        // copy file to file the meaning the destination must be a file.
        Assert.notNull(src, "Source must not null. ");
        Assert.notNull(dest, "Destination must not null. ");
        Assert.state(src.exists(), "Source must exists. ");
        Assert.state(src.isFile(), "Source must is a file. ");
        if (!dest.exists() && !dest.createNewFile()) {
            throw new IOException("Create destination file fail. ");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest, append);
            input  = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count;
            while (pos < size) {
                count = size - pos;
                count = count > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : count;
                pos += output.transferFrom(input, pos, count);
            }
        }
        finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }

        if (src.length() != dest.length()) {
            throw new IOException("Failed to copy full contents from '" + src + "' to '" + dest + "'");
        }
    }

    public static void copyFileToDirectory(File src, File dest, boolean append) throws IOException {
        if (!dest.exists() && !dest.mkdirs()) {
            throw new IOException("Destination directory mkdirs fail. ");
        }
        Assert.notNull(src, "Source must not null. ");
        String srcFileName = src.getName();
        Assert.notBlank(srcFileName, "Source file name must not blank. ");
        File destFile = new File(dest, srcFileName);
        FileUtils.copyFileToFile(src, destFile, append);
    }

    public static void copyDirectoryToDirectory(File src, File dest) throws IOException {
        Assert.notNull(src, "Source must not null. ");
        Assert.notNull(dest, "Destination must not null. ");
        Assert.state(src.exists(), "Source must exists. ");
        Assert.state(src.isDirectory(), "Source must is a file. ");
        if (!dest.exists() && !dest.mkdirs()) {
            throw new IOException("Destination directory mkdirs fail. ");
        }
        LinkedList<File> fileList = new LinkedList<File>();
        fileList.add(src);
        while (!fileList.isEmpty()) {
            File[] files = fileList.removeFirst().listFiles();
            if (ArrayUtils.isEmpty(files)) { continue; }
            for (File file : files) {
                String subPath = PathUtils.subPath(file, src);
                File destPath = new File(dest, subPath);
                if (file.isDirectory()) {
                    if (!destPath.exists() && !destPath.mkdirs()) {
                        throw new IOException("Directory[" + destPath.toString() + "] mkdirs fail. ");
                    }
                    fileList.addFirst(file);
                }
                else {
                    // The append is false meaning the destination directory is new.
                    FileUtils.copyFileToFile(file, destPath, false);
                }
            }
        }
    }

}
