package artoria.io.util;

import artoria.io.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.CloseUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import static artoria.common.Constants.ZERO;

/**
 * The file tools.
 * @author Kahle
 */
public class FileUtils {
    private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 10;
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean rename(File path, String newName) {
        File dest = new File(path.getParent(), newName);
        return path.renameTo(dest);
    }

    public static byte[] read(File destination) throws IOException {
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(destination.exists(), "Parameter \"destination\" must exists. ");
        Assert.state(destination.isFile(), "Parameter \"destination\" must be a file. ");
        FileInputStream in = null;
        try {
            in = new FileInputStream(destination);
            return IOUtils.toByteArray(in);
        }
        finally {
            CloseUtils.closeQuietly(in);
        }
    }

    public static long write(Object input, File destination) throws IOException {

        return FileUtils.write(input, destination, false);
    }

    public static long write(Object input, File destination, boolean append) throws IOException {
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        if (input == null) { return ZERO; }
        if (destination.exists()) {
            if (destination.isDirectory()) {
                throw new IOException("Parameter \"destination\" must be a file. ");
            }
        }
        else {
            FileUtils.createNewFile(destination);
        }
        FileOutputStream out = null;
        long count;
        try {
            out = new FileOutputStream(destination, append);
            if (input instanceof byte[]) {
                byte[] bytes = (byte[]) input;
                out.write(bytes);
                count = bytes.length;
            }
            else if (input instanceof InputStream) {
                count = IOUtils.copyLarge((InputStream) input, out);
            }
            else {
                throw new UnsupportedOperationException(
                        "Support only \"byte[]\" and \"InputStream\". ");
            }
            out.flush();
            return count;
        }
        finally {
            CloseUtils.closeQuietly(out);
        }
    }

    public static void createNewFile(File file) throws IOException {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        File parentFile = file.getParentFile();
        boolean notExist = parentFile != null && !parentFile.exists();
        if (notExist && !parentFile.mkdirs()) {
            throw new IOException("Create file parent directory \"" + parentFile + "\" fail. ");
        }
        if (!file.createNewFile()) {
            throw new IOException("Create file \"" + file + "\" fail. ");
        }
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
            // Don't have sub file or subdirectory, try delete.
            if (ArrayUtils.isEmpty(files)) {
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

    public static void moveFile(File source, File destination) throws IOException {
        // The append is false.
        // Meaning the destination directory not exists the source file name's file.
        FileUtils.copyFileToDirectory(source, destination, false);
        FileUtils.deleteFile(source);
    }

    public static void moveDirectory(File source, File destination) throws IOException {
        FileUtils.copyDirectoryToDirectory(source, destination);
        FileUtils.deleteDirectory(source);
    }

    public static void copyFileToFile(File source, File destination, boolean append) throws IOException {
        // Copy file to file the meaning the destination must be a file.
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(source.exists(), "Parameter \"source\" must exists. ");
        Assert.state(source.isFile(), "Parameter \"source\" must be a file. ");
        if (!destination.exists()) {
            File parentFile = destination.getParentFile();
            boolean noParent = parentFile != null && !parentFile.exists();
            if (noParent && !parentFile.mkdirs()) {
                throw new IOException("Create destination parent directory \"" + parentFile + "\" fail. ");
            }
            if (!destination.createNewFile()) {
                throw new IOException("Create destination file \"" + destination + "\" fail. ");
            }
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(destination, append);
            input  = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = ZERO;
            long count;
            while (pos < size) {
                count = size - pos;
                count = count > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : count;
                pos += output.transferFrom(input, pos, count);
            }
        }
        finally {
            CloseUtils.closeQuietly(output);
            CloseUtils.closeQuietly(fos);
            CloseUtils.closeQuietly(input);
            CloseUtils.closeQuietly(fis);
        }
        if (source.length() != destination.length()) {
            throw new IOException("Failed to copy full contents from \"" + source + "\" to \"" + destination + "\". ");
        }
    }

    public static void copyFileToDirectory(File source, File destination, boolean append) throws IOException {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        String sourceFileName = source.getName();
        Assert.notBlank(sourceFileName, "Source file name must not blank. ");
        File destFile = new File(destination, sourceFileName);
        FileUtils.copyFileToFile(source, destFile, append);
    }

    public static void copyDirectoryToDirectory(File source, File destination) throws IOException {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.state(source.exists(), "Parameter \"source\" must exists. ");
        Assert.state(source.isDirectory(), "Parameter \"source\" must be a directory. ");
        if (!destination.exists() && !destination.mkdirs()) {
            throw new IOException("Create destination directory \"" + destination + "\" fail. ");
        }
        LinkedList<File> fileList = new LinkedList<File>();
        fileList.add(source);
        while (!fileList.isEmpty()) {
            File[] files = fileList.removeFirst().listFiles();
            if (ArrayUtils.isEmpty(files)) { continue; }
            for (File file : files) {
                String subPath = FilenameUtils.subPath(file, source);
                File destPath = new File(destination, subPath);
                if (file.isDirectory()) {
                    if (!destPath.exists() && !destPath.mkdirs()) {
                        throw new IOException("Create directory \"" + destPath + "\" fail. ");
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
