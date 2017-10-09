package saber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class FileUtils {

    public static boolean mkdirs(String dirPath) {
        return new File(dirPath).mkdirs();
    }

    public static boolean mkdirs(File dirPath) {
        return dirPath.mkdirs();
    }

    public static boolean create(String filePath)
            throws IOException {
        return new File(filePath).createNewFile();
    }

    public static boolean create(File filePath)
            throws IOException {
        return filePath.createNewFile();
    }

    public static void delete(String dest)
            throws IOException {
        delete(new File(dest));
    }

    public static void delete(File dest)
            throws IOException {
        if (dest == null || !dest.exists()) return;
        if (dest.isDirectory()) {
            // stack model
            LinkedList<File> fileList = new LinkedList<>();
            fileList.addFirst(dest);
            while (!fileList.isEmpty()) {
                File current = fileList.removeFirst();
                File[] files = current.listFiles();
                // is add current ?
                boolean addCurrent = false;
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            if (!addCurrent) {
                                fileList.addFirst(current);
                                addCurrent = true;
                            }
                            fileList.addFirst(file);
                        } else {
                            if (!file.delete()) {
                                throw new IOException("File[" + file.toString() + "] delete failure. ");
                            }
                        }
                    }
                }
                if (!addCurrent && !current.delete()) {
                    throw new IOException("File[" + current.toString() + "] delete failure. ");
                }
            }
        } else {
            if (!dest.delete()) {
                throw new IOException("File[" + dest.toString() + "] delete failure. ");
            }
        }
    }

    public static void copy(String src, String dest)
            throws IOException {
        copy(new File(src), new File(dest));
    }

    public static void copy(File src, File dest)
            throws IOException {
        if (!src.exists()) {
            throw new IllegalArgumentException("Source not exist. ");
        } else if (src.isDirectory()) {
            if (!dest.exists() && !dest.mkdirs()) {
                throw new IllegalArgumentException("Make destination dir fail. ");
            }
            LinkedList<File> fileList = new LinkedList<>();
            fileList.add(src);
            while (!fileList.isEmpty()) {
                File[] files = fileList.removeFirst().listFiles();
                if (files == null || files.length == 0) continue;
                for (File file : files) {
                    String subPath = file.toString().replace(src.toString(), "");
                    File destPath = new File(dest, subPath);
                    if (file.isDirectory()) {
                        if (!destPath.exists() && !destPath.mkdirs()) {
                            throw new IOException("Directory[" + destPath.toString() + "] make failure. ");
                        }
                        fileList.addFirst(file);
                    } else {
                        copy(file, destPath);
                    }
                }
            }
        } else if (src.isFile()) {
            if (dest.exists() && dest.isFile()) {
                throw new IllegalArgumentException("Destination file already exist. ");
            }
            File destParent = new File(dest.getParent());
            if (!destParent.exists() && !destParent.mkdirs()) {
                throw new IllegalArgumentException("Make destination parent directory fail. ");
            }
            if (!dest.createNewFile()) {
                throw new IOException("File[" + dest.toString() + "] make failure. ");
            }
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(src);
                out = new FileOutputStream(dest);
                IOUtils.copyLarge(in, out);
            }
            finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {

                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException e) {

                    }
                }
            }
        }
    }

    public static void move(String src, String dest)
            throws IOException {
        move(new File(src), new File(dest));
    }

    public static void move(File src, File dest)
            throws IOException {
        copy(src, dest);
        delete(src);
    }

    public static boolean rename(String file, String newName) {
        return rename(new File(file), newName);
    }

    public static boolean rename(File file, String newName) {
        return file.renameTo(new File(file.getParent(), newName));
    }

    public static void write(File dest, byte[] data, boolean append)
            throws IOException {
        if(data != null && data.length > 0) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(dest, append);
                out.write(data);
                out.flush();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {

                    }
                }
            }
        }
    }

    public static byte[] read(File dest)
            throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(dest);
            return IOUtils.toByteArray(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {

                }
            }
        }
    }

}
