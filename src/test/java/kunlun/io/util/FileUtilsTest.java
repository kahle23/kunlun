/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Ignore
public class FileUtilsTest {
    private static Logger log = LoggerFactory.getLogger(FileUtilsTest.class);

    @Test
    public void testRenameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        log.info("{}", rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        log.info("{}", rnSc);
    }

    @Test
    public void testRename() {
        File file = new File("E:\\hello.txt");
        log.info("{}", FileUtils.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        log.info("{}", FileUtils.rename(file, "hello.txt"));
    }

    @Test
    public void testWrite() throws IOException {
        File destination = new File("e:\\test.txt");
        byte[] data = "Hello, World! ".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        FileUtils.write(in, destination);
//        FileUtils.write(data, destination);
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        File dest = new File("E:\\test");
        FileUtils.deleteDirectory(dest);
    }

    @Test
    public void testCopyFileToFile() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest\\11.txt");
        FileUtils.copyFileToFile(src, dest, false);
    }

    @Test
    public void testCopyFileToDirectory() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest");
        FileUtils.copyFileToDirectory(src, dest, false);
    }

    @Test
    public void testCopyDirectoryToDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.copyDirectoryToDirectory(src, dest);
    }

    @Test
    public void testMoveDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.moveDirectory(src, dest);
    }

}
