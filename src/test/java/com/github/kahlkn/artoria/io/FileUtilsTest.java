package com.github.kahlkn.artoria.io;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileUtilsTest {

    @Test
    public void testFindClasspath() throws Exception {
        File file = FileUtils.findClasspath("logging.properties");
        System.out.println(new String(FileUtils.read(file)));
        File file1 = FileUtils.findClasspath("hello.txt");
        System.out.println(file1);
    }

    @Test
    @Ignore
    public void testRenameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        System.out.println(rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        System.out.println(rnSc);
    }

    @Test
    @Ignore
    public void testRename() {
        File file = new File("E:\\hello.txt");
        System.out.println(FileUtils.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        System.out.println(FileUtils.rename(file, "hello.txt"));
    }

    @Test
    @Ignore
    public void testCopyFileToFile() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest\\11.txt");
        FileUtils.copyFileToFile(src, dest, false);
    }

    @Test
    @Ignore
    public void testCopyFileToDirectory() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest");
        FileUtils.copyFileToDirectory(src, dest, false);
    }

    @Test
    @Ignore
    public void testCopyDirectoryToDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.copyDirectoryToDirectory(src, dest);
    }

    @Test
    @Ignore
    public void testMoveDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.moveDirectory(src, dest);
    }

}
