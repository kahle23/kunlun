package com.apyhs.artoria.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileUtilsTest {

    @Test
    public void test() throws Exception {
        File file = FileUtils.findClasspath("logging.properties");
        System.out.println(new String(FileUtils.read(file)));
        File file1 = FileUtils.findClasspath("hello.txt");
        System.out.println(file1);
    }

    @Test
    public void test_renameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        System.out.println(rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        System.out.println(rnSc);
    }

    @Test
    public void test_rename() {
        File file = new File("E:\\hello.txt");
        System.out.println(FileUtils.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        System.out.println(FileUtils.rename(file, "hello.txt"));
    }

    @Test
    public void test_copyFileToFile() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest\\11.txt");
        FileUtils.copyFileToFile(src, dest, false);
    }

    @Test
    public void test_copyFileToDirectory() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest");
        FileUtils.copyFileToDirectory(src, dest, false);
    }

    @Test
    public void test_copyDirectoryToDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.copyDirectoryToDirectory(src, dest);
    }

    @Test
    public void test_moveDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.moveDirectory(src, dest);
    }

}
