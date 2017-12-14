package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Kahle
 */
public class FileUtils {
    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean rename(File path, String newName) {
        File dest = new File(path.getParent(), newName);
        return path.renameTo(dest);
    }

    public static byte[] read(File dest) throws IOException {
        // readFileToByteArray
        FileInputStream in = null;
        try {
            in = new FileInputStream(dest);
            return IOUtils.toByteArray(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static void write(byte[] data, File dest, boolean append) throws IOException {
        if (ArrayUtils.isEmpty(data)) { return; }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data);
            out.flush();
        }
        finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static boolean copyFileToFile(File src, File dest, boolean append) throws IOException {
        // copy file to file the meaning the dest must be a file.
        Assert.notNull(src, "Source is required. ");
        Assert.notNull(dest, "Destination is required. ");
        Assert.state(src.exists(), "Source must is exists. ");
        Assert.state(src.isFile(), "Source must is a file. ");

        if (!dest.exists() && !dest.createNewFile()) {
            throw new IOException("Create dest file fail. ");
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest, append);
            IOUtils.copyLarge(in, out);
            return true;
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public static boolean copyFileToDirectory(File src, File dest, boolean append) throws IOException {
        if (!dest.exists() && !dest.mkdirs()) {
            throw new IOException("Destination directory mkdirs fail. ");
        }
        Assert.notNull(src, "Source is required. ");
        String srcFileName = src.getName();
        boolean notBlank = StringUtils.isNotBlank(srcFileName);
        Assert.state(notBlank, "Source file name is blank. ");
        File destFile = new File(dest, srcFileName);
        return FileUtils.copyFileToFile(src, destFile, append);
    }

    public static boolean copyDirectoryToDirectory(File src, File dest) throws IOException {
        Assert.notNull(src, "Source is required. ");
        Assert.notNull(dest, "Destination is required. ");
        Assert.state(src.exists(), "Source must is exists. ");
        Assert.state(src.isDirectory(), "Source must is a file. ");

        if (!dest.exists() && !dest.mkdirs()) {
            throw new IOException("Destination directory mkdirs fail. ");
        }
        LinkedList<File> fileList = new LinkedList<>();
        fileList.add(src);
        while (!fileList.isEmpty()) {
            File[] files = fileList.removeFirst().listFiles();
            if (ArrayUtils.isEmpty(files)) { continue; }
            for (File file : files) {
                String subPath = PathUtils.subPath(file, src);
                File destPath = new File(dest, subPath);
                if (file.isDirectory()) {
                    if (!destPath.exists() && !destPath.mkdirs()) {
                        throw new IOException("Directory[" + destPath.toString() + "] mkdirs failure. ");
                    }
                    fileList.addFirst(file);
                }
                else {
                    // The append is false meaning the destination directory is new.
                    FileUtils.copyFileToFile(file, destPath, false);
                }
            }
        }
        return true;
    }

    public static boolean deleteFile(File dest) {
        return dest == null || !dest.exists() || dest.delete();
    }

    public static boolean deleteDirectory(File dest) throws IOException {
        Assert.notNull(dest, "Destination is required. ");
        Assert.state(dest.isDirectory(), "Destination must is a directory. ");
        // stack model
        boolean isSuccess = true;
        LinkedList<File> fileList = new LinkedList<>();
        fileList.addFirst(dest);
        while (!fileList.isEmpty()) {
            File current = fileList.removeFirst();
            File[] files = current.listFiles();
            // is add current ?
            boolean addCurrent = false;
            if (ArrayUtils.isNotEmpty(files)) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!addCurrent) {
                            fileList.addLast(current);
                            addCurrent = true;
                        }
                        fileList.addFirst(file);
                    }
                    else {
                        if (!file.delete()) {
                            log.info("File[" + file + "] delete failure. ");
                            isSuccess = false;
                        }
                    }
                }
            }
            if (!addCurrent && !current.delete()) {
                log.info("File[" + current + "] delete failure. ");
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public static boolean moveFile(File src, File dest) throws IOException {
        // The append is false meaning
        // the destination directory not exists the source file name's file.
        boolean b = FileUtils.copyFileToDirectory(src, dest, false);
        return b & FileUtils.deleteFile(src);
    }

    public static boolean moveDirectory(File src, File dest) throws IOException {
        boolean b = FileUtils.copyDirectoryToDirectory(src, dest);
        return b & FileUtils.deleteDirectory(src);
    }

}
